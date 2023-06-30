package com.example.roadmap.service;

import com.example.roadmap.config.exception.CEmailLoginFailedException;
import com.example.roadmap.domain.RefreshToken;
import com.example.roadmap.domain.User;
import com.example.roadmap.dto.AuthDTO;
import com.example.roadmap.dto.TokenDTO;
import com.example.roadmap.dto.UserDTO;
import com.example.roadmap.repository.RefreshTokenRepository;
import com.example.roadmap.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final SecurityService securityService;

    // 주요정보 분리(id, secret, uri)
    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${naver.client.secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;


    /**
     * 인가 코드로 카카오 access token 정보를 가져올 메소드
     */
    public AuthDTO.KakaoTokenResponse getKakaoAccessToken(String code) {
        RestTemplate rt = new RestTemplate(); //통신용
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
        params.add("client_id", KAKAO_CLIENT_ID); //카카오 앱 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", code); //인가 코드 요청시 받은 인가 코드값, 프론트에서 받아오는 그 코드

        // 헤더와 바디 합치기 위해 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 카카오로부터 Access token 수신
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (-> AuthDTO.KakaoTokenResponse)
        ObjectMapper objectMapper = new ObjectMapper();
        AuthDTO.KakaoTokenResponse kakaoTokenResponse = null;
        try {
            kakaoTokenResponse = objectMapper.readValue(accessTokenResponse.getBody(), AuthDTO.KakaoTokenResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoTokenResponse;
    }

    /**
     * kakaoAccessToken 으로 카카오 로그인 서버에서 회원 정보를 가져올 메소드
     */
    public AuthDTO.KakaoAccountResponse getKakaoInfo(String kakaoAccessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 보내고, response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        // JSON Parsing (-> AuthDTO.KakaoAccountResponse)
        ObjectMapper objectMapper = new ObjectMapper();
        AuthDTO.KakaoAccountResponse kakaoAccountResponse = null;
        try {
            kakaoAccountResponse = objectMapper.readValue(accountInfoResponse.getBody(), AuthDTO.KakaoAccountResponse.class);
        } catch (JsonProcessingException e) { e.printStackTrace(); }

        return kakaoAccountResponse;
    }

    /**
     * 회원가입 유무를 판별할 메소드
     */
    public ResponseEntity<AuthDTO.LoginResponse> kakaoLogin(String kakaoAccessToken) {
        // kakaoAccessToken 으로 카카오 회원정보를 받아오고
        AuthDTO.KakaoAccountResponse kakaoAccountResponse = getKakaoInfo(kakaoAccessToken);

        // 회원가입 유무를 판별할 loginResponse를 선언하고
        AuthDTO.LoginResponse loginResponse = new AuthDTO.LoginResponse();

        String email = kakaoAccountResponse.getKakao_account().getEmail();
        try {
            // 받아온 email로 로그인 성공
            TokenDTO.Request tokenDto = securityService.login(email);
            loginResponse.setLoginSuccess(true);

            // 회원정보를 받아와 UserDTO.Response 형태로 매핑하여 설정
            UserDTO.Response userResponse = new UserDTO.Response(userRepository.findByEmail(email)
                    .orElseThrow(CEmailLoginFailedException::new));
            loginResponse.setUserResponse(userResponse);
            HttpHeaders headers = setTokenHeaders(tokenDto);
            return ResponseEntity.ok().headers(headers).body(loginResponse);
        } catch(CEmailLoginFailedException e) {
            // 로그인 실패(비회원)
            // 받아온 회원정보를 UserDTO.Request로 빌드 후 UserDTO.Response로 매핑한 뒤 설정
            UserDTO.Request userRequest = UserDTO.Request.builder()
                    .nickName(kakaoAccountResponse.getKakao_account().getProfile().getNickname())
                    .email(kakaoAccountResponse.getKakao_account().getEmail())
                    .build();
            UserDTO.Response userResponse = new UserDTO.Response(userRequest.toEntity());
            loginResponse.setUserResponse(userResponse);
            loginResponse.setLoginSuccess(false);
            return ResponseEntity.ok(loginResponse);
        }
    }

    /**
     * 인가 코드로 네이버 access token 정보를 가져올 메소드
     */
    public AuthDTO.NaverTokenResponse getNaverAccessToken(String code) {
        RestTemplate rt = new RestTemplate(); //통신용
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 객체 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NAVER_CLIENT_ID); //네이버 앱 REST API 키
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("redirect_uri", NAVER_REDIRECT_URI);
        params.add("code", code); //인가 코드 요청시 받은 인가 코드값, 프론트에서 받아오는 그 코드

        // 헤더와 바디 합치기 위해 HttpEntity 객체 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        // 네이버로부터 Access token 수신
        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // JSON Parsing (-> AuthDTO.NaverTokenResponse)
        ObjectMapper objectMapper = new ObjectMapper();
        AuthDTO.NaverTokenResponse naverTokenResponse = null;
        try {
            naverTokenResponse = objectMapper.readValue(accessTokenResponse.getBody(), AuthDTO.NaverTokenResponse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return naverTokenResponse;
    }

    /**
     * naverAccessToken 으로 네이버 로그인 서버에서 회원 정보를 가져올 메소드
     */
    public AuthDTO.NaverAccountResponse getNaverInfo(String naverAccessToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + naverAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

        // POST 방식으로 API 서버에 요청 보내고, response 받아옴
        ResponseEntity<String> accountInfoResponse = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                accountInfoRequest,
                String.class
        );

        // JSON Parsing (-> AuthDTO.NaverAccountResponse)
        ObjectMapper objectMapper = new ObjectMapper();
        AuthDTO.NaverAccountResponse naverAccountResponse = null;
        try {
            naverAccountResponse = objectMapper.readValue(accountInfoResponse.getBody(), AuthDTO.NaverAccountResponse.class);
        } catch (JsonProcessingException e) { e.printStackTrace(); }

        return naverAccountResponse;
    }

    /**
     * 회원가입 유무를 판별할 메소드
     */
    public ResponseEntity<AuthDTO.LoginResponse> naverLogin(String naverAccessToken) {
        // naverAccessToken 으로 네이버 회원정보를 받아오고
        AuthDTO.NaverAccountResponse naverAccountResponse = getNaverInfo(naverAccessToken);

        // 회원가입 유무를 판별할 loginResponse를 선언하고
        AuthDTO.LoginResponse loginResponse = new AuthDTO.LoginResponse();

        String email = naverAccountResponse.getResponse().getEmail();
        try {
            // 받아온 email로 로그인 성공
            TokenDTO.Request tokenDto = securityService.login(email);
            loginResponse.setLoginSuccess(true);

            // 회원정보를 받아와 UserDTO.Response 형태로 매핑하여 설정
            UserDTO.Response userResponse = new UserDTO.Response(userRepository.findByEmail(email)
                    .orElseThrow(CEmailLoginFailedException::new));
            loginResponse.setUserResponse(userResponse);
            HttpHeaders headers = setTokenHeaders(tokenDto);
            return ResponseEntity.ok().headers(headers).body(loginResponse);
        } catch(CEmailLoginFailedException e) {
            // 로그인 실패(비회원)
            // 받아온 회원정보를 UserDTO.Request로 빌드 후 UserDTO.Response로 매핑한 뒤 설정
            UserDTO.Request userRequest = UserDTO.Request.builder()
                    .nickName(naverAccountResponse.getResponse().getNickname())
                    .email(naverAccountResponse.getResponse().getEmail())
                    .build();
            UserDTO.Response userResponse = new UserDTO.Response(userRequest.toEntity());
            loginResponse.setUserResponse(userResponse);
            loginResponse.setLoginSuccess(false);
            return ResponseEntity.ok(loginResponse);
        }
    }

    /**
     * 토큰을 헤더에 배치
     */
    public HttpHeaders setTokenHeaders(TokenDTO.Request tokenDto) {
        HttpHeaders headers = new HttpHeaders();
        ResponseCookie cookie = ResponseCookie.from("RefreshToken", tokenDto.getRefreshToken())
                .path("/")
                .maxAge(60*60*24*7) // 쿠키 유효기간 7일로 설정했음
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        headers.add("Set-cookie", cookie.toString());
        headers.add("Authorization", tokenDto.getAccessToken());

        return headers;
    }

    /**
     * 소셜 로그인
     */
    public ResponseEntity<AuthDTO.LoginResponse> socialLogin(String registrationId, String code){
        if(registrationId.equals("kakao")) {
            String kakaoAccessToken = getKakaoAccessToken(code).getAccess_token();
            return kakaoLogin(kakaoAccessToken);
        }else {
            String naverAccessToken = getNaverAccessToken(code).getAccess_token();
            return naverLogin(naverAccessToken);
        }
    }

    /**
     * Refresh Token 을 Repository 에 저장하는 메소드
     */
    public void saveRefreshToken(User user, TokenDTO.Request tokenDto) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getUserId())
                .token(tokenDto.getRefreshToken())
                .build();
        tokenRepository.save(refreshToken);
        System.out.println("토큰 저장이 완료되었습니다");
    }

    /**
     * 회원 가입
     */
    public ResponseEntity<AuthDTO.SignupResponse> save(UserDTO.Request dto) {
        // 넘어온 dto를 엔티티로 바꿔 User에 저장
        User user = dto.toEntity();
        userRepository.save(user);

        // 회원가입 상황에 대해 토큰을 발급하고 헤더와 쿠키에 배치
        TokenDTO.Request tokenDto = securityService.signup(dto);
        saveRefreshToken(user, tokenDto);
        HttpHeaders headers = setTokenHeaders(tokenDto);

        // 응답 작성
        AuthDTO.SignupResponse responseDto = new AuthDTO.SignupResponse();
        // 회원정보를 받아와 UserDTO.Response 형태로 매핑하여 설정
        UserDTO.Response userResponse = new UserDTO.Response(userRepository.findByEmail(dto.getEmail())
                .orElseThrow(CEmailLoginFailedException::new));
        responseDto.setUserResponse(userResponse);
        responseDto.setSignUpSuccess(true);
        return ResponseEntity.ok().headers(headers).body(responseDto);
    }
}

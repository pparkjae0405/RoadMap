package com.example.roadmap.service;

import com.example.roadmap.domain.User;
import com.example.roadmap.dto.AuthDTO;
import com.example.roadmap.dto.UserDTO;
import com.example.roadmap.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor // final 혹은 @NotNull이 붙은 필드의 생성자를 자동으로 만들어준다
public class AuthService {
    private final UserRepository userRepository;

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
    public AuthDTO.LoginResponse kakaoLogin(String kakaoAccessToken) {
        // kakaoAccessToken 으로 카카오 회원정보를 받아오고
        AuthDTO.KakaoAccountResponse kakaoAccountResponse = getKakaoInfo(kakaoAccessToken);

        // 회원가입 유무를 판별할 loginResponse를 선언하고
        AuthDTO.LoginResponse loginResponse = new AuthDTO.LoginResponse();

        // 받아온 회원정보에서 email을 가져와 가입되어 있는지 확인하여
        String kakaoEmail = kakaoAccountResponse.getKakao_account().getEmail();
        if (userRepository.existsByEmail(kakaoEmail)) {
            // 가입된 사용자라면 true + 해당 회원정보(UserDTO.Response로 매핑)
            loginResponse.setLoginSuccess(true);
            UserDTO.Response userResponse = new UserDTO.Response(userRepository.findByEmail(kakaoEmail));
            loginResponse.setUserResponse(userResponse);
        }else {
            // 가입되지 않은 사용자라면 false + 불러온 닉네임, 이메일을 UserDTO.Request로 빌드 후
            UserDTO.Request userRequest = UserDTO.Request.builder()
                    .nickName(kakaoAccountResponse.getKakao_account().getProfile().getNickname())
                    .email(kakaoEmail)
                    .build();

            // UserDTO.Response로 매핑
            UserDTO.Response userResponse = new UserDTO.Response(userRequest.toEntity());
            loginResponse.setUserResponse(userResponse);
        }
        return loginResponse;
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
    public AuthDTO.LoginResponse naverLogin(String naverAccessToken) {
        // naverAccessToken 으로 네이버 회원정보를 받아오고
        AuthDTO.NaverAccountResponse naverAccountResponse = getNaverInfo(naverAccessToken);
        // 회원가입 유무를 판별할 loginResponse를 선언하고
        AuthDTO.LoginResponse loginResponse = new AuthDTO.LoginResponse();

        // 받아온 회원정보에서 email을 가져와 가입되어 있는지 확인하여
        String naverEmail = naverAccountResponse.getResponse().getEmail();
        if (userRepository.existsByEmail(naverEmail)) {
            // 가입된 사용자라면 true + 해당 회원정보(UserDTO.Response로 매핑)
            loginResponse.setLoginSuccess(true);
            UserDTO.Response userResponse = new UserDTO.Response(userRepository.findByEmail(naverEmail));
            loginResponse.setUserResponse(userResponse);
        }else {
            // 가입되지 않은 사용자라면 false + 불러온 닉네임, 이메일을 UserDTO.Request로 빌드 후
            UserDTO.Request userRequest = UserDTO.Request.builder()
                    .nickName(naverAccountResponse.getResponse().getNickname())
                    .email(naverEmail)
                    .build();

            // UserDTO.Response로 매핑
            UserDTO.Response userResponse = new UserDTO.Response(userRequest.toEntity());
            loginResponse.setUserResponse(userResponse);
        }
        return loginResponse;
    }

    /**
     * 소셜 로그인
     */
    public AuthDTO.LoginResponse socialLogin(String registrationId, String code){
        if(registrationId.equals("kakao")) {
            String kakaoAccessToken = getKakaoAccessToken(code).getAccess_token();
            return kakaoLogin(kakaoAccessToken);
        }else {
            String naverAccessToken = getNaverAccessToken(code).getAccess_token();
            return naverLogin(naverAccessToken);
        }
    }

    /**
     * 회원 가입
     */
    public Long save(UserDTO.Request dto) {
        // 넘어온 dto를 엔티티로 바꿔 User에 저장
        User user = dto.toEntity();
        userRepository.save(user);

        return user.getUserId();
    }
}

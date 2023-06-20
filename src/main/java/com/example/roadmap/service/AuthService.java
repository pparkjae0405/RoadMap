package com.example.roadmap.service;

import com.example.roadmap.dto.AuthDTO;
import com.example.roadmap.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
        params.add("client_id", "d33e1d44ed690e28149969fab4686b61"); //카카오 앱 REST API 키
        params.add("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao");
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
}

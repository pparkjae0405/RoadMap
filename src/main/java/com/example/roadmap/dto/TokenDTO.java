package com.example.roadmap.dto;

import lombok.*;

public class TokenDTO {
    /**
     * 토큰을 발급할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String grantType;
        private String accessToken;
        private String refreshToken;
    }

    /**
     * accessToken을 재발급할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ReissueTokenRequest {
        private String accessToken;
    }
}

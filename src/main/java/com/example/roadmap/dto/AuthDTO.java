package com.example.roadmap.dto;

import lombok.*;

import java.util.Properties;

public class AuthDTO {
    /**
     * 카카오 access token 정보를 리턴할 응답(Response) 클래스
     */
    @Getter
    @RequiredArgsConstructor
    public static class KakaoTokenResponse {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }

    /**
     * 카카오 회원정보를 리턴할 응답(Response) 클래스
     */
    @Getter
    @RequiredArgsConstructor
    public static class KakaoAccountResponse {
        private Long id; //회원번호, *Required*
        private String connected_at; //서비스에 연결된 시각, UTC*
        private Properties properties;
        private KakaoAccount kakao_account;

        @Getter
        @RequiredArgsConstructor
        public class KakaoAccount {
            private Boolean profile_nickname_needs_agreement;
            private KakaoProfile profile;
            private Boolean has_email;
            private Boolean email_needs_agreement;
            private Boolean is_email_valid;
            private Boolean is_email_verified;
            private String email;

            @Getter
            @RequiredArgsConstructor
            public class KakaoProfile {
                private String nickname;
            }
        }
    }
}

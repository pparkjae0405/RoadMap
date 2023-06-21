package com.example.roadmap.dto;

import com.example.roadmap.domain.User;
import lombok.*;

public class UserDTO {
    /**
     * 회원의 등록과 수정을 처리할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long userId;
        private String nickName;
        private String email;
        private String major;

        /* Dto -> Entity */
        public User toEntity(){
            User user = User.builder()
                    .userId(userId)
                    .nickName(nickName)
                    .email(email)
                    .major(major)
                    .build();

            return user;
        }
    }

    /**
     * 회원 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private Long userId;
        private String nickName;
        private String email;
        private String major;

        public Response(User user){
            this.userId = user.getUserId();
            this.nickName = user.getNickName();
            this.email = user.getEmail();
            this.major = user.getMajor();
        }
    }
}

package com.example.roadmap.dto;

import com.example.roadmap.domain.Info;
import com.example.roadmap.domain.Roadmap;
import lombok.*;

public class InfoDTO {
    /**
     * 댓글의 등록과 수정을 처리할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long infoId;
        private String date;
        private String title;
        private String content;
        private Roadmap roadmap;

        /* Dto -> Entity */
        public Info toEntity() {
            Info infos = Info.builder()
                    .infoId(infoId)
                    .date(date)
                    .title(title)
                    .content(content)
                    .roadmap(roadmap)
                    .build();

            return infos;
        }
    }

    /**
     * 댓글 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private String date;
        private String title;
        private String content;

        /* Entity -> Dto*/
        public Response(Info info) {
            this.date = info.getDate();
            this.title = info.getTitle();
            this.content = info.getContent();
        }
    }

    /**
     * 기본 수행 결과를 리턴할 응답(Response) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResultResponse {
        private boolean success;
    }
}

package com.example.roadmap.dto;

import com.example.roadmap.domain.Roadmap;
import com.example.roadmap.domain.Tag;
import lombok.*;

public class TagDTO {
    /**
     * 태그의 등록과 수정을 처리할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long tagId;
        private String tag;
        private Roadmap roadmap;

        /* Dto -> Entity */
        public Tag toEntity() {
            Tag tags = Tag.builder()
                    .tagId(tagId)
                    .tag(tag)
                    .roadmap(roadmap)
                    .build();

            return tags;
        }
    }

    /**
     * 태그 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private Long tagId;
        private String tag;

        /* Entity -> Dto*/
        public Response(Tag tag) {
            this.tagId = tag.getTagId();
            this.tag = tag.getTag();
        }
    }
}

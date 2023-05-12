package com.example.roadmap.dto;

import com.example.roadmap.domain.Roadmap;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class RoadmapDTO {
    /**
     * 게시글의 등록과 수정을 처리할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long roadmapId;
        private String date;
        private Long view;
        private String title;
        private String content;

        /* Dto -> Entity */
        public Roadmap toEntity() {
            Roadmap roadmap = Roadmap.builder()
                    .roadmapId(roadmapId)
                    .date(date)
                    .view(view)
                    .title(title)
                    .content(content)
                    .build();

            return roadmap;
        }
    }

    /**
     * 게시글 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
    @Getter
    @RequiredArgsConstructor
    public static class Response{
        private Long roadmapId;
        private String date;
        private Long view;
        private String title;
        private String content;
        private List<InfoDTO.Response> infos;
        private List<CommentDTO.Response> comments;

        /* Entity -> Dto*/
        public Response(Roadmap roadmap) {
            this.roadmapId = roadmap.getRoadmapId();
            this.date = roadmap.getDate();
            this.view = roadmap.getView();
            this.title = roadmap.getTitle();
            this.content = roadmap.getContent();
            this.infos = roadmap.getInfos()
                    .stream()
                    .map(InfoDTO.Response::new)
                    .collect(Collectors.toList());
            this.comments = roadmap.getComments()
                    .stream()
                    .map(CommentDTO.Response::new)
                    .collect(Collectors.toList());
            /*
            .stream() : 컬렉션에 저장되어있는 요소들을 하나씩 참조하여 람다식으로 처리하는 반복자
            .map() : 값을 변환해주는 람다식을 인자로 받아 새로운 데이터를 만들기 위해 사용
            .collect() : 스트림의 데이터를 변형 등의 처리를 하고 원하는 자료형으로 변환
            Collectors.toList() : 스트림의 요소들을 모아 List 인스턴스 반환
             */
        }
    }
}

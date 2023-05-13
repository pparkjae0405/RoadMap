package com.example.roadmap.dto;

import com.example.roadmap.domain.Comment;
import com.example.roadmap.domain.Roadmap;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentDTO {
    /**
     * 댓글의 등록과 수정을 처리할 요청(Request) 클래스
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long commentId;
        private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        private String content;
        private Roadmap roadmap;

        /* Dto -> Entity */
        public Comment toEntity() {
            Comment comments = Comment.builder()
                    .commentId(commentId)
                    .date(date)
                    .content(content)
                    .roadmap(roadmap)
                    .build();

            return comments;
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
        private Long commentId;
        private String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        private String content;

        /* Entity -> Dto*/
        public Response(Comment comment) {
            this.commentId = comment.getCommentId();
            this.date = comment.getDate();
            this.content = comment.getContent();
        }
    }
}

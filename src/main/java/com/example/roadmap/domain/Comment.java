package com.example.roadmap.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 댓글 작성을 위한 Comment 테이블
 */
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
@Getter
@Entity
public class Comment extends BaseTime{
    /**
     * 댓글 번호(PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK AutoIncrement
    @Column(name = "commentId")
    private Long commentId;

    /**
     * 댓글 작성 날짜(BaseTime에서 상속), 댓글 내용
     */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 글 번호, 회원 번호(FK)
     */
    // ManyToOne의 기본값은 EAGER이기에 LAZY로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmapId") // 지정된 이름으로 외래 키 매핑
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    /**
     * 댓글 수정
     */
    public void update(String content) {
        this.content = content;
    }
}

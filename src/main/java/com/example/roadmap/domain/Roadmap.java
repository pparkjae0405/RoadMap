package com.example.roadmap.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 글 작성을 위한 Roadmap 테이블
 */
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
@Getter
@Entity
public class Roadmap {
    /**
     * 글 번호(PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK AutoIncrement
    private Long roadmapId;

    /**
     * 글 작성 날짜, 조회수, 글 제목, 글 내용
     */
    private String date;
    private Long view;
    private String title;
    private String content;

    /**
     * Roadmap은 Info와 일대다 연관관계를 가짐
     */
    // mappedBy = "roadmap" : FK는 다 쪽에서 관리
    // cascade = cascadeType.ALL : 상위 엔터티에서 하위 엔터티로 모든 작업을 전파
    // orphanRemoval = true : 부모 엔티티가 삭제되면 자식 엔티티도 삭제
    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Info> infos;

    /**
     * Roadmap과 Comment는 일대다 연관관계를 가짐
     */
    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    /**
     * 글 수정
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

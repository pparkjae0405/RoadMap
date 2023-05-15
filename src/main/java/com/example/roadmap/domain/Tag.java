package com.example.roadmap.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 글 작성 시 태그 작성을 위한 Tag 테이블
 */
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
@Getter
@Entity
public class Tag {
    /**
     * 태그 번호(PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tagId")
    private Long tagId;

    /**
     * 태그
     */
    @Column(nullable = false)
    private String tag;

    /**
     * 글 번호(FK)
     */
    // ManyToOne의 기본값은 EAGER이기에 LAZY로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmapId") // 지정된 이름으로 외래 키 매핑
    private Roadmap roadmap;

    /**
     * 태그 수정
     */
    public void update(String tag){
        this.tag = tag;
    }
}

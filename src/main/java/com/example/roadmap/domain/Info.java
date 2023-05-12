package com.example.roadmap.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 글 작성 시 로드맵 작성을 위한 Info 테이블
 */
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
@Getter
@Entity
public class Info {
    /**
     * 로드맵 번호(PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long infoId;

    /**
     * 로드맵 시작 날짜, 로드맵 제목, 로드맵 내용
     */
    private String date;
    private String title;
    private String content;

    /**
     * 글 번호(FK)
     */
    // ManyToOne의 기본값은 EAGER이기에 LAZY로 변경
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmapId") // 지정된 이름으로 외래 키 매핑
    private Roadmap roadmap;

    /**
     * 로드맵 수정
     */
    public void update(String date, String title, String content){
        this.date = date;
        this.title = title;
        this.content = content;
    }
}

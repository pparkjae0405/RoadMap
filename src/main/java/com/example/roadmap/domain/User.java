package com.example.roadmap.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 회원정보를 위한 User 테이블
 */
@Builder
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 만듦
@Getter
@Entity
public class User {
    /**
     * 회원 번호(PK)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    /**
     * 닉네임, 이메일, 직무, 권한
     */
    @Column(name = "nickName", nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column
    private String major;

    @Column
    @Enumerated(EnumType.STRING)
    private Authority authority;

    /**
     * User는 Roadmap, Comment와 일대다 연관관계를 가짐
     */
    // mappedBy = "user" : FK는 다 쪽에서 관리
    // cascade = cascadeType.ALL : 상위 엔터티에서 하위 엔터티로 모든 작업을 전파
    // orphanRemoval = true : 부모 엔티티가 삭제되면 자식 엔티티도 삭제
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Roadmap> roadmaps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    /**
     * 회원정보 수정
     */
    public void update(String nickName, String major) {
        this.nickName = nickName;
        this.major = major;
    }
}

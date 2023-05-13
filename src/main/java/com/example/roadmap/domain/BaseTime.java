package com.example.roadmap.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 공통적으로 사용되는 컬럼이므로, 이를 상속한 클래스에서 컬럼을 추가
 */

@Getter
@MappedSuperclass // Entity 클래스가 BaseTime 을 상속받을 때, createdDate, modifiedDate 를 인식할 수 있도록 하는 설정
@EntityListeners(AuditingEntityListener.class) // 자동으로 값을 넣어주도록 하는 설정
public class BaseTime {
    @Column(nullable = false)
    @CreatedDate // 데이터 생성할 때 시간 자동 생성
    private String date;

    /* 해당 엔티티를 저장하기 이전에 실행 */
    @PrePersist
    public void onPrePersist(){
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}

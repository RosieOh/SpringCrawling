package com.crawling.core.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 시간 정보만 포함하는 기본 엔티티 클래스
 * 생성일시와 수정일시만 필요한 엔티티에서 사용합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    
    /**
     * 생성 일시
     * 엔티티가 생성된 시간을 자동으로 기록합니다.
     */
    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 수정 일시
     * 엔티티가 마지막으로 수정된 시간을 자동으로 기록합니다.
     */
    @LastModifiedDate
    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}

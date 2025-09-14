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
 * 모든 엔티티의 기본 클래스
 * 공통 필드와 기능을 제공합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    
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
    
    /**
     * 생성자
     * 엔티티를 생성한 사용자의 식별자를 기록합니다.
     */
    @Column(name = "CREATED_BY", length = 50, updatable = false)
    private String createdBy;
    
    /**
     * 수정자
     * 엔티티를 마지막으로 수정한 사용자의 식별자를 기록합니다.
     */
    @Column(name = "UPDATED_BY", length = 50)
    private String updatedBy;
    
    /**
     * 삭제 여부
     * 논리 삭제를 위한 플래그입니다.
     */
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted = false;
    
    /**
     * 삭제 일시
     * 논리 삭제된 시간을 기록합니다.
     */
    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;
    
    /**
     * 삭제자
     * 엔티티를 삭제한 사용자의 식별자를 기록합니다.
     */
    @Column(name = "DELETED_BY", length = 50)
    private String deletedBy;
    
    /**
     * 엔티티를 논리 삭제합니다.
     * 
     * @param deletedBy 삭제자
     */
    public void delete(String deletedBy) {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
    
    /**
     * 엔티티를 복구합니다.
     */
    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
        this.deletedBy = null;
    }
    
    /**
     * 삭제 여부를 확인합니다.
     * 
     * @return 삭제 여부
     */
    public boolean isDeleted() {
        return Boolean.TRUE.equals(this.isDeleted);
    }
}

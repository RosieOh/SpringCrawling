package com.crawling.core.enums;

/**
 * 상담 상태 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum CounselingStatus {
    
    /**
     * 대기 중
     */
    PENDING("대기 중"),
    
    /**
     * 승인됨
     */
    APPROVED("승인됨"),
    
    /**
     * 거절됨
     */
    REJECTED("거절됨"),
    
    /**
     * 완료됨
     */
    COMPLETED("완료됨"),
    
    /**
     * 취소됨
     */
    CANCELLED("취소됨"),
    
    /**
     * 진행 중
     */
    IN_PROGRESS("진행 중");
    
    private final String description;
    
    CounselingStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

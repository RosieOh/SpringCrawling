package com.crawling.core.enums;

/**
 * 챌린지 상태 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum ChallengeStatus {
    
    /**
     * 대기 중
     */
    PENDING("대기 중"),
    
    /**
     * 활성
     */
    ACTIVE("활성"),
    
    /**
     * 완료됨
     */
    COMPLETED("완료됨"),
    
    /**
     * 일시정지
     */
    PAUSED("일시정지"),
    
    /**
     * 취소됨
     */
    CANCELLED("취소됨"),
    
    /**
     * 종료됨
     */
    ENDED("종료됨");
    
    private final String description;
    
    ChallengeStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

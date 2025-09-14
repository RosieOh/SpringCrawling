package com.crawling.core.enums;

/**
 * 반복 타입 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum RecurrenceType {
    
    /**
     * 반복 없음
     */
    NONE("반복 없음"),
    
    /**
     * 매일
     */
    DAILY("매일"),
    
    /**
     * 매주
     */
    WEEKLY("매주"),
    
    /**
     * 매월
     */
    MONTHLY("매월"),
    
    /**
     * 매년
     */
    YEARLY("매년"),
    
    /**
     * 평일만
     */
    WEEKDAYS("평일만"),
    
    /**
     * 주말만
     */
    WEEKENDS("주말만"),
    
    /**
     * 사용자 정의
     */
    CUSTOM("사용자 정의");
    
    private final String description;
    
    RecurrenceType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

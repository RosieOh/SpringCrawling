package com.crawling.core.enums;

/**
 * 이벤트 카테고리 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum EventCategory {
    
    /**
     * 스터디
     */
    STUDY("스터디"),
    
    /**
     * 모임
     */
    MEETING("모임"),
    
    /**
     * 워크샵
     */
    WORKSHOP("워크샵"),
    
    /**
     * 세미나
     */
    SEMINAR("세미나"),
    
    /**
     * 컨퍼런스
     */
    CONFERENCE("컨퍼런스"),
    
    /**
     * 네트워킹
     */
    NETWORKING("네트워킹"),
    
    /**
     * 프로젝트
     */
    PROJECT("프로젝트"),
    
    /**
     * 기타
     */
    OTHER("기타");
    
    private final String description;
    
    EventCategory(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

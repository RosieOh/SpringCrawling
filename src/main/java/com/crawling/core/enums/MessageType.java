package com.crawling.core.enums;

/**
 * 메시지 타입 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum MessageType {
    
    /**
     * 텍스트 메시지
     */
    TEXT("텍스트"),
    
    /**
     * 이미지 메시지
     */
    IMAGE("이미지"),
    
    /**
     * 파일 메시지
     */
    FILE("파일"),
    
    /**
     * 시스템 메시지
     */
    SYSTEM("시스템"),
    
    /**
     * 공지사항
     */
    ANNOUNCEMENT("공지사항"),
    
    /**
     * 이모지
     */
    EMOJI("이모지"),
    
    /**
     * 링크
     */
    LINK("링크");
    
    private final String description;
    
    MessageType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

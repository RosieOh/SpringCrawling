package com.crawling.core.enums;

/**
 * 뱃지 희귀도 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum BadgeRarity {
    
    /**
     * 일반
     */
    COMMON("일반"),
    
    /**
     * 희귀
     */
    RARE("희귀"),
    
    /**
     * 에픽
     */
    EPIC("에픽"),
    
    /**
     * 레전더리
     */
    LEGENDARY("레전더리"),
    
    /**
     * 신화
     */
    MYTHIC("신화");
    
    private final String description;
    
    BadgeRarity(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

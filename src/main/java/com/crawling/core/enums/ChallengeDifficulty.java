package com.crawling.core.enums;

/**
 * 챌린지 난이도 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum ChallengeDifficulty {
    
    /**
     * 초급
     */
    BEGINNER("초급"),
    
    /**
     * 중급
     */
    INTERMEDIATE("중급"),
    
    /**
     * 고급
     */
    ADVANCED("고급"),
    
    /**
     * 전문가
     */
    EXPERT("전문가"),
    
    /**
     * 마스터
     */
    MASTER("마스터");
    
    private final String description;
    
    ChallengeDifficulty(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

package com.crawling.core.enums;

/**
 * 과목을 정의하는 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum Subject {
    
    /**
     * 국어
     */
    KOREAN("KOREAN", "국어"),
    
    /**
     * 수학
     */
    MATH("MATH", "수학"),
    
    /**
     * 영어
     */
    ENGLISH("ENGLISH", "영어"),
    
    /**
     * 사회
     */
    SOCIAL("SOCIAL", "사회"),
    
    /**
     * 과학
     */
    SCIENCE("SCIENCE", "과학"),
    
    /**
     * 역사
     */
    HISTORY("HISTORY", "역사"),
    
    /**
     * 지리
     */
    GEOGRAPHY("GEOGRAPHY", "지리"),
    
    /**
     * 물리
     */
    PHYSICS("PHYSICS", "물리"),
    
    /**
     * 화학
     */
    CHEMISTRY("CHEMISTRY", "화학"),
    
    /**
     * 생물
     */
    BIOLOGY("BIOLOGY", "생물"),
    
    /**
     * 지구과학
     */
    EARTH_SCIENCE("EARTH_SCIENCE", "지구과학"),
    
    /**
     * 음악
     */
    MUSIC("MUSIC", "음악"),
    
    /**
     * 미술
     */
    ART("ART", "미술"),
    
    /**
     * 체육
     */
    PHYSICAL_EDUCATION("PHYSICAL_EDUCATION", "체육"),
    
    /**
     * 기술
     */
    TECHNOLOGY("TECHNOLOGY", "기술"),
    
    /**
     * 가정
     */
    HOME_ECONOMICS("HOME_ECONOMICS", "가정"),
    
    /**
     * 정보
     */
    COMPUTER_SCIENCE("COMPUTER_SCIENCE", "정보"),
    
    /**
     * 기타
     */
    OTHER("OTHER", "기타");
    
    /**
     * 과목 코드
     */
    private final String code;
    
    /**
     * 과목 설명
     */
    private final String description;
    
    /**
     * Subject 생성자
     * 
     * @param code 과목 코드
     * @param description 과목 설명
     */
    Subject(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 과목 코드를 반환합니다.
     * 
     * @return 과목 코드
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 과목 설명을 반환합니다.
     * 
     * @return 과목 설명
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 코드로 Subject를 찾습니다.
     * 
     * @param code 과목 코드
     * @return Subject 객체
     * @throws IllegalArgumentException 해당 코드의 과목이 없을 때
     */
    public static Subject fromCode(String code) {
        for (Subject subject : values()) {
            if (subject.code.equals(code)) {
                return subject;
            }
        }
        throw new IllegalArgumentException("Unknown subject code: " + code);
    }
    
    /**
     * 과목이 주요 과목인지 확인합니다.
     * 
     * @return 주요 과목 여부
     */
    public boolean isMajorSubject() {
        return this == KOREAN || this == MATH || this == ENGLISH;
    }
    
    /**
     * 과목이 과학 과목인지 확인합니다.
     * 
     * @return 과학 과목 여부
     */
    public boolean isScienceSubject() {
        return this == SCIENCE || this == PHYSICS || this == CHEMISTRY || 
               this == BIOLOGY || this == EARTH_SCIENCE;
    }
}

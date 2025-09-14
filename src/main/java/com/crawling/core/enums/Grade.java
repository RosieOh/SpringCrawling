package com.crawling.core.enums;

/**
 * 학년을 정의하는 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum Grade {
    
    /**
     * 유치원
     */
    KINDERGARTEN("KINDERGARTEN", "유치원"),
    
    /**
     * 초등학교 1학년
     */
    ELEMENTARY_1("ELEMENTARY_1", "초등학교 1학년"),
    
    /**
     * 초등학교 2학년
     */
    ELEMENTARY_2("ELEMENTARY_2", "초등학교 2학년"),
    
    /**
     * 초등학교 3학년
     */
    ELEMENTARY_3("ELEMENTARY_3", "초등학교 3학년"),
    
    /**
     * 초등학교 4학년
     */
    ELEMENTARY_4("ELEMENTARY_4", "초등학교 4학년"),
    
    /**
     * 초등학교 5학년
     */
    ELEMENTARY_5("ELEMENTARY_5", "초등학교 5학년"),
    
    /**
     * 초등학교 6학년
     */
    ELEMENTARY_6("ELEMENTARY_6", "초등학교 6학년"),
    
    /**
     * 중학교 1학년
     */
    MIDDLE_1("MIDDLE_1", "중학교 1학년"),
    
    /**
     * 중학교 2학년
     */
    MIDDLE_2("MIDDLE_2", "중학교 2학년"),
    
    /**
     * 중학교 3학년
     */
    MIDDLE_3("MIDDLE_3", "중학교 3학년"),
    
    /**
     * 고등학교 1학년
     */
    HIGH_1("HIGH_1", "고등학교 1학년"),
    
    /**
     * 고등학교 2학년
     */
    HIGH_2("HIGH_2", "고등학교 2학년"),
    
    /**
     * 고등학교 3학년
     */
    HIGH_3("HIGH_3", "고등학교 3학년");
    
    /**
     * 학년 코드
     */
    private final String code;
    
    /**
     * 학년 설명
     */
    private final String description;
    
    /**
     * Grade 생성자
     * 
     * @param code 학년 코드
     * @param description 학년 설명
     */
    Grade(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 학년 코드를 반환합니다.
     * 
     * @return 학년 코드
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 학년 설명을 반환합니다.
     * 
     * @return 학년 설명
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 코드로 Grade를 찾습니다.
     * 
     * @param code 학년 코드
     * @return Grade 객체
     * @throws IllegalArgumentException 해당 코드의 학년이 없을 때
     */
    public static Grade fromCode(String code) {
        for (Grade grade : values()) {
            if (grade.code.equals(code)) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Unknown grade code: " + code);
    }
    
    /**
     * 학년이 초등학교인지 확인합니다.
     * 
     * @return 초등학교 여부
     */
    public boolean isElementary() {
        return this.code.startsWith("ELEMENTARY");
    }
    
    /**
     * 학년이 중학교인지 확인합니다.
     * 
     * @return 중학교 여부
     */
    public boolean isMiddle() {
        return this.code.startsWith("MIDDLE");
    }
    
    /**
     * 학년이 고등학교인지 확인합니다.
     * 
     * @return 고등학교 여부
     */
    public boolean isHigh() {
        return this.code.startsWith("HIGH");
    }
}

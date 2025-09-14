package com.crawling.core.enums;

/**
 * 사용자 역할을 정의하는 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum UserRole {
    
    /**
     * 일반 사용자 (학부모)
     */
    USER("USER", "일반 사용자"),
    
    /**
     * 관리자
     */
    ADMIN("ADMIN", "관리자"),
    
    /**
     * 슈퍼 관리자
     */
    SUPER_ADMIN("SUPER_ADMIN", "슈퍼 관리자");
    
    /**
     * 역할 코드
     */
    private final String code;
    
    /**
     * 역할 설명
     */
    private final String description;
    
    /**
     * UserRole 생성자
     * 
     * @param code 역할 코드
     * @param description 역할 설명
     */
    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 역할 코드를 반환합니다.
     * 
     * @return 역할 코드
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 역할 설명을 반환합니다.
     * 
     * @return 역할 설명
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 코드로 UserRole을 찾습니다.
     * 
     * @param code 역할 코드
     * @return UserRole 객체
     * @throws IllegalArgumentException 해당 코드의 역할이 없을 때
     */
    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.code.equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }
}

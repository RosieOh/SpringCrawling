package com.crawling.core.jpa.constant;

/**
 * JPA 관련 상수 클래스
 * JPA에서 사용되는 공통 상수들을 정의합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
public final class JpaConstants {
    
    // 생성자 숨김
    private JpaConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // ========== 컬럼명 상수 ==========
    
    /**
     * ID 컬럼명
     */
    public static final String ID_COLUMN = "ID";
    
    /**
     * 생성 일시 컬럼명
     */
    public static final String CREATED_AT_COLUMN = "CREATED_AT";
    
    /**
     * 수정 일시 컬럼명
     */
    public static final String UPDATED_AT_COLUMN = "UPDATED_AT";
    
    /**
     * 생성자 컬럼명
     */
    public static final String CREATED_BY_COLUMN = "CREATED_BY";
    
    /**
     * 수정자 컬럼명
     */
    public static final String UPDATED_BY_COLUMN = "UPDATED_BY";
    
    /**
     * 삭제 여부 컬럼명
     */
    public static final String IS_DELETED_COLUMN = "IS_DELETED";
    
    /**
     * 삭제 일시 컬럼명
     */
    public static final String DELETED_AT_COLUMN = "DELETED_AT";
    
    /**
     * 삭제자 컬럼명
     */
    public static final String DELETED_BY_COLUMN = "DELETED_BY";
    
    // ========== 컬럼 길이 상수 ==========
    
    /**
     * 기본 문자열 길이
     */
    public static final int DEFAULT_STRING_LENGTH = 255;
    
    /**
     * 긴 문자열 길이
     */
    public static final int LONG_STRING_LENGTH = 1000;
    
    /**
     * 매우 긴 문자열 길이
     */
    public static final int VERY_LONG_STRING_LENGTH = 2000;
    
    /**
     * 사용자 ID 길이
     */
    public static final int USER_ID_LENGTH = 50;
    
    /**
     * 이메일 길이
     */
    public static final int EMAIL_LENGTH = 100;
    
    /**
     * 전화번호 길이
     */
    public static final int PHONE_LENGTH = 20;
    
    /**
     * URL 길이
     */
    public static final int URL_LENGTH = 500;
    
    // ========== 테이블명 상수 ==========
    
    /**
     * 사용자 테이블명
     */
    public static final String USER_TABLE = "TBL_USERS";
    
    /**
     * 게시글 테이블명
     */
    public static final String POST_TABLE = "TBL_POSTS";
    
    /**
     * 댓글 테이블명
     */
    public static final String COMMENT_TABLE = "TBL_COMMENTS";
    
    /**
     * 단축 URL 테이블명
     */
    public static final String SHORT_URL_TABLE = "TBL_SHORT_URLS";
    
    /**
     * URL 접근 로그 테이블명
     */
    public static final String URL_ACCESS_LOG_TABLE = "TBL_URL_ACCESS_LOGS";
    
    // ========== 쿼리 상수 ==========
    
    /**
     * 삭제되지 않은 엔티티 조회 조건
     */
    public static final String NOT_DELETED_CONDITION = "e.isDeleted = false";
    
    /**
     * 삭제된 엔티티 조회 조건
     */
    public static final String DELETED_CONDITION = "e.isDeleted = true";
    
    /**
     * 활성 상태 조건
     */
    public static final String ACTIVE_CONDITION = "e.status = 'ACTIVE'";
    
    /**
     * 비활성 상태 조건
     */
    public static final String INACTIVE_CONDITION = "e.status = 'INACTIVE'";
    
    // ========== 정렬 상수 ==========
    
    /**
     * 생성일시 내림차순 정렬
     */
    public static final String ORDER_BY_CREATED_AT_DESC = "ORDER BY e.createdAt DESC";
    
    /**
     * 수정일시 내림차순 정렬
     */
    public static final String ORDER_BY_UPDATED_AT_DESC = "ORDER BY e.updatedAt DESC";
    
    /**
     * ID 내림차순 정렬
     */
    public static final String ORDER_BY_ID_DESC = "ORDER BY e.id DESC";
    
    /**
     * 이름 오름차순 정렬
     */
    public static final String ORDER_BY_NAME_ASC = "ORDER BY e.name ASC";
    
    // ========== 페이징 상수 ==========
    
    /**
     * 기본 페이지 크기
     */
    public static final int DEFAULT_PAGE_SIZE = 20;
    
    /**
     * 최대 페이지 크기
     */
    public static final int MAX_PAGE_SIZE = 100;
    
    /**
     * 최소 페이지 크기
     */
    public static final int MIN_PAGE_SIZE = 1;
    
    // ========== 기타 상수 ==========
    
    /**
     * 시스템 사용자
     */
    public static final String SYSTEM_USER = "system";
    
    /**
     * 익명 사용자
     */
    public static final String ANONYMOUS_USER = "anonymous";
    
    /**
     * 기본 활성화 상태
     */
    public static final Boolean DEFAULT_ACTIVE_STATUS = true;
    
    /**
     * 기본 비활성화 상태
     */
    public static final Boolean DEFAULT_INACTIVE_STATUS = false;
}

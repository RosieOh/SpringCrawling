package com.crawling.core.constant;

/**
 * 애플리케이션 전반에서 사용되는 공통 상수들을 정의하는 클래스
 * 
 * @author tspoon
 * @version 1.0
 */
public final class CommonConstants {
    
    // ========== 페이지네이션 관련 ==========
    /**
     * 기본 페이지 크기
     */
    public static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 최대 페이지 크기
     */
    public static final int MAX_PAGE_SIZE = 100;
    
    /**
     * 기본 페이지 번호 (0부터 시작)
     */
    public static final int DEFAULT_PAGE_NUMBER = 0;
    
    // ========== 파일 업로드 관련 ==========
    /**
     * 최대 파일 크기 (10MB)
     */
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    /**
     * 허용되는 이미지 확장자
     */
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
    
    /**
     * 허용되는 문서 확장자
     */
    public static final String[] ALLOWED_DOCUMENT_EXTENSIONS = {".pdf", ".doc", ".docx", ".hwp", ".txt"};
    
    // ========== 검색 관련 ==========
    /**
     * 최소 검색어 길이
     */
    public static final int MIN_SEARCH_KEYWORD_LENGTH = 2;
    
    /**
     * 최대 검색어 길이
     */
    public static final int MAX_SEARCH_KEYWORD_LENGTH = 50;
    
    // ========== 랭킹 관련 ==========
    /**
     * 랭킹 표시 개수
     */
    public static final int RANKING_DISPLAY_COUNT = 10;
    
    /**
     * 인기 콘텐츠 표시 개수
     */
    public static final int POPULAR_CONTENT_COUNT = 5;
    
    // ========== 스터디 매칭 관련 ==========
    /**
     * 최대 스터디 그룹 인원
     */
    public static final int MAX_STUDY_GROUP_SIZE = 8;
    
    /**
     * 최소 스터디 그룹 인원
     */
    public static final int MIN_STUDY_GROUP_SIZE = 2;
    
    // ========== 알림 관련 ==========
    /**
     * 알림 유지 기간 (일)
     */
    public static final int NOTIFICATION_RETENTION_DAYS = 30;
    
    // ========== 캐시 관련 ==========
    /**
     * 기본 캐시 TTL (초)
     */
    public static final int DEFAULT_CACHE_TTL = 300;
    
    /**
     * 인기 콘텐츠 캐시 TTL (초)
     */
    public static final int POPULAR_CONTENT_CACHE_TTL = 1800;
    
    // ========== 정렬 관련 ==========
    /**
     * 기본 정렬 기준
     */
    public static final String DEFAULT_SORT_BY = "createdAt";
    
    /**
     * 기본 정렬 방향
     */
    public static final String DEFAULT_SORT_DIRECTION = "desc";
    
    // ========== 상태 관련 ==========
    /**
     * 활성 상태
     */
    public static final String STATUS_ACTIVE = "ACTIVE";
    
    /**
     * 비활성 상태
     */
    public static final String STATUS_INACTIVE = "INACTIVE";
    
    /**
     * 삭제 상태
     */
    public static final String STATUS_DELETED = "DELETED";
    
    // 생성자 숨김
    private CommonConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}

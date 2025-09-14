package com.crawling.core.jpa.exception;

import com.tspoon.global.error.ErrorCode;
import com.tspoon.global.exception.BusinessException;

/**
 * JPA 관련 예외 클래스
 * JPA 작업 중 발생하는 예외를 처리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
public class JpaException extends BusinessException {
    
    /**
     * JpaException 생성자
     * 
     * @param errorCode 에러 코드
     */
    public JpaException(ErrorCode errorCode) {
        super(errorCode);
    }
    
    /**
     * JpaException 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 커스텀 메시지
     */
    public JpaException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
    
    /**
     * JpaException 생성자
     * 
     * @param errorCode 에러 코드
     * @param message 커스텀 메시지
     * @param cause 원인 예외
     */
    public JpaException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}

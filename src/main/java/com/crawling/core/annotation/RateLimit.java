package com.crawling.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API 호출 빈도를 제한하기 위한 어노테이션
 * AOP를 통해 메서드 호출 빈도를 제한하고, 제한을 초과하면 예외를 발생시킵니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    
    /**
     * 시간당 최대 호출 횟수
     * 기본값: 100회
     */
    int maxRequests() default 100;
    
    /**
     * 시간 윈도우 (초 단위)
     * 기본값: 3600초 (1시간)
     */
    int timeWindow() default 3600;
    
    /**
     * 키 생성 전략
     * IP: IP 주소 기반
     * USER: 사용자 ID 기반
     * CUSTOM: 커스텀 키 생성
     */
    KeyStrategy keyStrategy() default KeyStrategy.IP;
    
    /**
     * 커스텀 키 생성 표현식
     * keyStrategy가 CUSTOM일 때 사용하는 SpEL 표현식
     */
    String keyExpression() default "";
    
    /**
     * 에러 메시지
     * 제한 초과 시 사용할 커스텀 메시지
     */
    String message() default "요청이 너무 많습니다. 잠시 후 다시 시도해주세요.";
    
    /**
     * 키 생성 전략 열거형
     */
    enum KeyStrategy {
        /**
         * IP 주소 기반
         */
        IP,
        
        /**
         * 사용자 ID 기반
         */
        USER,
        
        /**
         * 커스텀 키 생성
         */
        CUSTOM
    }
}

package com.crawling.core.annotation;

import com.tspoon.core.enums.UserRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 특정 역할을 가진 사용자만 접근할 수 있도록 제한하는 어노테이션
 * AOP를 통해 메서드 실행 전에 사용자의 역할을 검증합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    
    /**
     * 허용된 역할 목록
     * 이 중 하나라도 해당되면 접근 허용
     */
    UserRole[] value();
    
    /**
     * 에러 메시지
     * 역할 검증 실패 시 사용할 커스텀 메시지
     */
    String message() default "접근 권한이 없습니다.";
}

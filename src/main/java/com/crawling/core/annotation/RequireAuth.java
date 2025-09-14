package com.crawling.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증된 사용자만 접근할 수 있도록 제한하는 어노테이션
 * AOP를 통해 메서드 실행 전에 사용자 인증 상태를 검증합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAuth {
    
    /**
     * 에러 메시지
     * 인증 실패 시 사용할 커스텀 메시지
     */
    String message() default "인증이 필요합니다.";
}

package com.crawling.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메서드 실행 후에 권한을 검증하기 위한 어노테이션
 * AOP를 통해 메서드 실행 후에 SpEL 표현식으로 권한을 검증합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostAuthorize {
    
    /**
     * 권한 검증 표현식
     * SpEL 표현식으로 권한을 검증
     * 예: "returnObject.owner == authentication.name"
     */
    String value();
    
    /**
     * 에러 메시지
     * 권한 검증 실패 시 사용할 커스텀 메시지
     */
    String message() default "접근 권한이 없습니다.";
}

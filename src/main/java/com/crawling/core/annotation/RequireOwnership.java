package com.crawling.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 리소스 소유자만 접근할 수 있도록 제한하는 어노테이션
 * AOP를 통해 메서드 실행 전에 리소스 소유권을 검증합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireOwnership {
    
    /**
     * 리소스 ID 파라미터명
     * 메서드 파라미터 중 리소스 ID를 나타내는 파라미터의 이름
     */
    String resourceIdParam() default "id";
    
    /**
     * 리소스 타입
     * 검증할 리소스의 타입 (예: "question", "answer", "material")
     */
    String resourceType();
    
    /**
     * 에러 메시지
     * 소유권 검증 실패 시 사용할 커스텀 메시지
     */
    String message() default "해당 리소스에 대한 접근 권한이 없습니다.";
}

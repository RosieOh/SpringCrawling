package com.crawling.core.socket.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WebSocket 인증 필요 어노테이션
 * WebSocket 연결 시 인증이 필요한 메서드에 사용합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireWebSocketAuth {
    
    /**
     * 필요한 권한
     * 
     * @return 권한 배열
     */
    String[] roles() default {};
    
    /**
     * 인증 실패 시 메시지
     * 
     * @return 에러 메시지
     */
    String message() default "인증이 필요합니다.";
}

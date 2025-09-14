package com.crawling.core.socket.annotation;

import com.tspoon.core.socket.enums.MessageType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WebSocket 메시지 매핑 어노테이션
 * 특정 메시지 타입에 대한 핸들러 메서드를 지정합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketMessageMapping {
    
    /**
     * 처리할 메시지 타입
     * 
     * @return 메시지 타입
     */
    MessageType value();
    
    /**
     * 메시지 우선순위 (낮을수록 높은 우선순위)
     * 
     * @return 우선순위
     */
    int priority() default 0;
}

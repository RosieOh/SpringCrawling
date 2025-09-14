package com.crawling.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 현재 로그인한 사용자 정보를 주입받기 위한 어노테이션
 * 컨트롤러 메서드의 파라미터에 사용하여 현재 사용자 정보를 자동으로 주입받을 수 있습니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {
    
    /**
     * 필수 여부
     * true: 사용자 정보가 없으면 예외 발생
     * false: 사용자 정보가 없어도 null 허용
     */
    boolean required() default true;
}

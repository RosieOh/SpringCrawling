package com.crawling.core.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JPA Auditing을 위한 어노테이션
 * 이 어노테이션이 적용된 엔티티는 자동으로 생성/수정 정보를 기록합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    
    /**
     * 생성 일시 컬럼명
     * 기본값: "CREATED_AT"
     */
    String createdAtColumn() default "CREATED_AT";
    
    /**
     * 수정 일시 컬럼명
     * 기본값: "UPDATED_AT"
     */
    String updatedAtColumn() default "UPDATED_AT";
    
    /**
     * 생성자 컬럼명
     * 기본값: "CREATED_BY"
     */
    String createdByColumn() default "CREATED_BY";
    
    /**
     * 수정자 컬럼명
     * 기본값: "UPDATED_BY"
     */
    String updatedByColumn() default "UPDATED_BY";
}

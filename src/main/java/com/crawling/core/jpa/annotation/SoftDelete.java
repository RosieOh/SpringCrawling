package com.crawling.core.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 논리 삭제를 위한 어노테이션
 * 이 어노테이션이 적용된 엔티티는 논리 삭제 기능을 사용할 수 있습니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SoftDelete {
    
    /**
     * 삭제 플래그 컬럼명
     * 기본값: "IS_DELETED"
     */
    String deletedFlagColumn() default "IS_DELETED";
    
    /**
     * 삭제 일시 컬럼명
     * 기본값: "DELETED_AT"
     */
    String deletedAtColumn() default "DELETED_AT";
    
    /**
     * 삭제자 컬럼명
     * 기본값: "DELETED_BY"
     */
    String deletedByColumn() default "DELETED_BY";
}

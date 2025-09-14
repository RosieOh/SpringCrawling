package com.crawling.core.jpa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

/**
 * JPA 설정 클래스
 * JPA 관련 공통 설정을 관리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.tspoon.domain")
public class JpaConfig {
    
    /**
     * JPA Auditing을 위한 AuditorAware 빈
     * @CreatedBy, @LastModifiedBy 어노테이션에서 사용할 현재 사용자 정보를 제공합니다.
     * 
     * @return AuditorAware 구현체
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                // SecurityContextHolder.getContext().getAuthentication().getName() 등
                return Optional.of("system");
            }
        };
    }
}

package com.crawling.core.aspect;

import com.tspoon.core.annotation.RateLimit;
import com.tspoon.global.error.ErrorCode;
import com.tspoon.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Rate Limiting을 위한 Aspect
 * API 호출 빈도를 제한합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    
    private final RedisTemplate<String, String> redisTemplate;
    
    /**
     * @RateLimit 어노테이션이 적용된 메서드의 호출 빈도를 제한합니다.
     */
    @Around("@annotation(com.tspoon.core.annotation.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        RateLimit annotation = getAnnotation(joinPoint, RateLimit.class);
        
        String key = generateKey(joinPoint, annotation);
        String currentCount = redisTemplate.opsForValue().get(key);
        
        int count = currentCount == null ? 0 : Integer.parseInt(currentCount);
        
        if (count >= annotation.maxRequests()) {
            log.warn("Rate limit exceeded for key: {}", key);
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS, annotation.message());
        }
        
        // 카운트 증가
        if (count == 0) {
            redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(annotation.timeWindow()));
        } else {
            redisTemplate.opsForValue().increment(key);
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * Rate limit 키를 생성합니다.
     */
    private String generateKey(ProceedingJoinPoint joinPoint, RateLimit annotation) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append("rate_limit:");
        keyBuilder.append(joinPoint.getSignature().toShortString());
        
        switch (annotation.keyStrategy()) {
            case IP -> {
                // TODO: 실제 IP 주소 가져오기
                keyBuilder.append(":ip:127.0.0.1");
            }
            case USER -> {
                // TODO: 현재 사용자 ID 가져오기
                keyBuilder.append(":user:1");
            }
            case CUSTOM -> {
                // TODO: 커스텀 키 생성 로직 구현
                keyBuilder.append(":custom:").append(annotation.keyExpression());
            }
        }
        
        return keyBuilder.toString();
    }
    
    /**
     * 어노테이션을 가져옵니다.
     */
    @SuppressWarnings("unchecked")
    private <T> T getAnnotation(ProceedingJoinPoint joinPoint, Class<T> annotationClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return (T) method.getAnnotation((Class<? extends java.lang.annotation.Annotation>) annotationClass);
    }
}

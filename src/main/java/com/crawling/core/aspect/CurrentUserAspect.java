package com.crawling.core.aspect;

import com.tspoon.core.annotation.CurrentUser;
import com.tspoon.domain.user.entity.User;
import com.tspoon.global.error.ErrorCode;
import com.tspoon.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 현재 사용자 주입을 위한 Aspect
 * @CurrentUser 어노테이션이 적용된 파라미터에 현재 사용자를 주입합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CurrentUserAspect {
    
    /**
     * @CurrentUser 어노테이션이 적용된 파라미터에 현재 사용자를 주입합니다.
     */
    @Around("@annotation(com.tspoon.core.annotation.CurrentUser) || @within(com.tspoon.core.annotation.CurrentUser)")
    public Object injectCurrentUser(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        
        // @CurrentUser 어노테이션이 적용된 파라미터 찾기
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(CurrentUser.class)) {
                CurrentUser annotation = parameters[i].getAnnotation(CurrentUser.class);
                User currentUser = getCurrentUser(annotation.required());
                args[i] = currentUser;
            }
        }
        
        return joinPoint.proceed(args);
    }
    
    /**
     * 현재 사용자를 가져옵니다.
     * 
     * @param required 필수 여부
     * @return 현재 사용자 (필수가 아닌 경우 null 가능)
     */
    private User getCurrentUser(boolean required) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            
            if (required) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
            }
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        
        if (required) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "사용자 정보를 찾을 수 없습니다.");
        }
        
        return null;
    }
}

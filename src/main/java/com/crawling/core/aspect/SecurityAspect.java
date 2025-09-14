package com.crawling.core.aspect;

import com.tspoon.core.annotation.RequireAuth;
import com.tspoon.core.annotation.RequireOwnership;
import com.tspoon.core.annotation.RequireRole;
import com.tspoon.core.enums.UserRole;
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
 * 보안을 위한 Aspect
 * 인증, 권한, 소유권 검증을 관리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecurityAspect {
    
    /**
     * @RequireAuth 어노테이션이 적용된 메서드의 인증을 검증합니다.
     */
    @Around("@annotation(com.tspoon.core.annotation.RequireAuth)")
    public Object requireAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        RequireAuth annotation = getAnnotation(joinPoint, RequireAuth.class);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, annotation.message());
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * @RequireRole 어노테이션이 적용된 메서드의 역할을 검증합니다.
     */
    @Around("@annotation(com.tspoon.core.annotation.RequireRole)")
    public Object requireRole(ProceedingJoinPoint joinPoint) throws Throwable {
        RequireRole annotation = getAnnotation(joinPoint, RequireRole.class);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
        }
        
        User user = getCurrentUser(authentication);
        UserRole userRole = user.getRole();
        
        boolean hasRequiredRole = false;
        for (UserRole requiredRole : annotation.value()) {
            if (userRole == requiredRole) {
                hasRequiredRole = true;
                break;
            }
        }
        
        if (!hasRequiredRole) {
            throw new BusinessException(ErrorCode.FORBIDDEN, annotation.message());
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * @RequireOwnership 어노테이션이 적용된 메서드의 소유권을 검증합니다.
     */
    @Around("@annotation(com.tspoon.core.annotation.RequireOwnership)")
    public Object requireOwnership(ProceedingJoinPoint joinPoint) throws Throwable {
        RequireOwnership annotation = getAnnotation(joinPoint, RequireOwnership.class);
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "인증이 필요합니다.");
        }
        
        User currentUser = getCurrentUser(authentication);
        
        // 리소스 ID 파라미터에서 소유자 확인
        Long resourceId = getResourceId(joinPoint, annotation.resourceIdParam());
        if (resourceId == null) {
            throw new BusinessException(ErrorCode.INVALID_PARAMETER, "리소스 ID를 찾을 수 없습니다.");
        }
        
        // TODO: 실제 소유권 검증 로직 구현
        // 예: 데이터베이스에서 리소스의 소유자 확인
        boolean isOwner = checkResourceOwnership(resourceId, currentUser.getId(), annotation.resourceType());
        
        if (!isOwner) {
            throw new BusinessException(ErrorCode.FORBIDDEN, annotation.message());
        }
        
        return joinPoint.proceed();
    }
    
    /**
     * 현재 사용자를 가져옵니다.
     */
    private User getCurrentUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        throw new BusinessException(ErrorCode.UNAUTHORIZED, "사용자 정보를 찾을 수 없습니다.");
    }
    
    /**
     * 리소스 ID를 가져옵니다.
     */
    private Long getResourceId(ProceedingJoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] args = joinPoint.getArgs();
        
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(paramName) && args[i] instanceof Long) {
                return (Long) args[i];
            }
        }
        
        return null;
    }
    
    /**
     * 리소스 소유권을 확인합니다.
     * 
     * @param resourceId 리소스 ID
     * @param userId 사용자 ID
     * @param resourceType 리소스 타입
     * @return 소유권 여부
     */
    private boolean checkResourceOwnership(Long resourceId, Long userId, String resourceType) {
        // TODO: 실제 소유권 검증 로직 구현
        // 예시: 데이터베이스에서 리소스의 소유자 확인
        
        log.debug("Checking ownership for resourceId: {}, userId: {}, resourceType: {}", 
                resourceId, userId, resourceType);
        
        // 임시로 항상 true 반환 (실제 구현 필요)
        // 실제로는 해당 리소스 타입에 따라 적절한 서비스를 호출하여 소유권 확인
        switch (resourceType.toLowerCase()) {
            case "board":
                // BoardService를 통해 게시글 소유권 확인
                // return boardService.isOwner(resourceId, userId);
                return true;
            case "comment":
                // CommentService를 통해 댓글 소유권 확인
                // return commentService.isOwner(resourceId, userId);
                return true;
            case "study":
                // StudyService를 통해 스터디 소유권 확인
                // return studyService.isOwner(resourceId, userId);
                return true;
            case "user":
                // 사용자 자신의 리소스인지 확인
                return resourceId.equals(userId);
            default:
                log.warn("Unknown resource type: {}", resourceType);
                return false;
        }
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

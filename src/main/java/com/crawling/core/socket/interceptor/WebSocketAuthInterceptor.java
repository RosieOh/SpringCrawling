package com.crawling.core.socket.interceptor;

import com.tspoon.core.socket.util.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * WebSocket 인증 인터셉터
 * WebSocket 연결 시 사용자 인증을 처리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    
    /**
     * WebSocket 핸드셰이크 전에 호출됩니다.
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param wsHandler WebSocket 핸들러
     * @param attributes WebSocket 세션 속성
     * @return 핸드셰이크 진행 여부
     * @throws Exception 예외 발생 시
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                 WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        try {
            // URL에서 사용자 ID 추출 (예: /ws/chat?userId=123)
            String userId = WebSocketUtils.extractUserIdFromRequest(request);
            
            if (userId == null || userId.trim().isEmpty()) {
                log.warn("WebSocket connection rejected: No user ID provided");
                return false;
            }
            
            // 사용자 인증 검증 (실제 구현에서는 JWT 토큰 검증 등 수행)
            if (!isValidUser(userId)) {
                log.warn("WebSocket connection rejected: Invalid user ID: {}", userId);
                return false;
            }
            
            // 세션 속성에 사용자 ID 저장
            attributes.put("userId", userId);
            log.info("WebSocket handshake successful for user: {}", userId);
            
            return true;
            
        } catch (Exception e) {
            log.error("Error during WebSocket handshake", e);
            return false;
        }
    }
    
    /**
     * WebSocket 핸드셰이크 후에 호출됩니다.
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param wsHandler WebSocket 핸들러
     * @param exception 발생한 예외 (있는 경우)
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                             WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket handshake failed", exception);
        }
    }
    
    /**
     * 사용자 ID가 유효한지 검증합니다.
     * 
     * @param userId 사용자 ID
     * @return 유효성 여부
     */
    private boolean isValidUser(String userId) {
        // TODO: 실제 사용자 인증 로직 구현
        // 예: JWT 토큰 검증, 데이터베이스에서 사용자 존재 확인 등
        
        // 임시로 null이 아니고 빈 문자열이 아닌 경우 유효하다고 판단
        return userId != null && !userId.trim().isEmpty();
    }
}

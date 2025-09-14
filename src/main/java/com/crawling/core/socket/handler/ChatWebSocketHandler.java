package com.crawling.core.socket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tspoon.core.socket.dto.SocketMessage;
import com.tspoon.core.socket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 채팅 WebSocket 핸들러
 * WebSocket 연결, 메시지 수신, 연결 해제를 처리합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {
    
    private final WebSocketService webSocketService;
    private final ObjectMapper objectMapper;
    
    /**
     * 활성 WebSocket 세션들을 저장하는 맵
     * Key: 사용자 ID, Value: WebSocket 세션
     */
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    /**
     * WebSocket 연결이 성공했을 때 호출됩니다.
     * 
     * @param session WebSocket 세션
     * @throws Exception 예외 발생 시
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.put(userId, session);
            webSocketService.handleUserConnect(userId, session);
            log.info("User {} connected to WebSocket", userId);
        } else {
            log.warn("Failed to establish WebSocket connection: No user ID found");
            session.close(CloseStatus.BAD_DATA.withReason("No user ID found"));
        }
    }
    
    /**
     * WebSocket 메시지를 수신했을 때 호출됩니다.
     * 
     * @param session WebSocket 세션
     * @param message 수신된 메시지
     * @throws Exception 예외 발생 시
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        try {
            String userId = getUserIdFromSession(session);
            if (userId == null) {
                log.warn("Received message from unknown user");
                return;
            }
            
            // 텍스트 메시지를 SocketMessage 객체로 변환
            if (message instanceof TextMessage) {
                String payload = ((TextMessage) message).getPayload();
                SocketMessage socketMessage = objectMapper.readValue(payload, SocketMessage.class);
                socketMessage.setSenderId(userId);
                
                // 메시지 처리
                webSocketService.handleMessage(socketMessage, session);
            }
            
        } catch (Exception e) {
            log.error("Error handling WebSocket message", e);
            sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * WebSocket 연결에 오류가 발생했을 때 호출됩니다.
     * 
     * @param session WebSocket 세션
     * @param exception 발생한 예외
     * @throws Exception 예외 발생 시
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String userId = getUserIdFromSession(session);
        log.error("WebSocket transport error for user {}: {}", userId, exception.getMessage());
        
        if (userId != null) {
            webSocketService.handleUserDisconnect(userId);
            sessions.remove(userId);
        }
    }
    
    /**
     * WebSocket 연결이 종료되었을 때 호출됩니다.
     * 
     * @param session WebSocket 세션
     * @param closeStatus 연결 종료 상태
     * @throws Exception 예외 발생 시
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            webSocketService.handleUserDisconnect(userId);
            sessions.remove(userId);
            log.info("User {} disconnected from WebSocket. Status: {}", userId, closeStatus);
        }
    }
    
    /**
     * WebSocket이 부분적인 메시지를 지원하는지 여부를 반환합니다.
     * 
     * @return false (부분 메시지 미지원)
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    
    /**
     * 세션에서 사용자 ID를 추출합니다.
     * 
     * @param session WebSocket 세션
     * @return 사용자 ID
     */
    private String getUserIdFromSession(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        return (String) attributes.get("userId");
    }
    
    /**
     * 특정 사용자에게 메시지를 전송합니다.
     * 
     * @param userId 사용자 ID
     * @param message 전송할 메시지
     */
    public void sendMessageToUser(String userId, SocketMessage message) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String payload = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(payload));
            } catch (Exception e) {
                log.error("Failed to send message to user {}: {}", userId, e.getMessage());
            }
        } else {
            log.warn("User {} is not connected or session is closed", userId);
        }
    }
    
    /**
     * 에러 메시지를 전송합니다.
     * 
     * @param session WebSocket 세션
     * @param errorMessage 에러 메시지
     */
    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage("{\"type\":\"ERROR\",\"content\":\"" + errorMessage + "\"}"));
            }
        } catch (Exception e) {
            log.error("Failed to send error message", e);
        }
    }
    
    /**
     * 현재 연결된 사용자 수를 반환합니다.
     * 
     * @return 연결된 사용자 수
     */
    public int getConnectedUserCount() {
        return sessions.size();
    }
    
    /**
     * 특정 사용자가 연결되어 있는지 확인합니다.
     * 
     * @param userId 사용자 ID
     * @return 연결 여부
     */
    public boolean isUserConnected(String userId) {
        WebSocketSession session = sessions.get(userId);
        return session != null && session.isOpen();
    }
}

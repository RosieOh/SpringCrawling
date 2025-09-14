package com.crawling.core.socket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tspoon.core.socket.dto.ChatMessage;
import com.tspoon.core.socket.dto.NotificationMessage;
import com.tspoon.core.socket.handler.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

/**
 * Redis 메시지 구독자
 * Redis Pub/Sub을 통해 수신된 메시지를 WebSocket으로 전달합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisMessageSubscriber implements MessageListener {
    
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ObjectMapper objectMapper;
    
    /**
     * Redis에서 메시지를 수신했을 때 호출됩니다.
     * 
     * @param message Redis 메시지
     * @param pattern 패턴 (사용하지 않음)
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String payload = new String(message.getBody());
            
            log.debug("Received message from channel {}: {}", channel, payload);
            
            // 채널에 따라 메시지 처리
            if (channel.contains("chat")) {
                handleChatMessage(payload);
            } else if (channel.contains("notification")) {
                handleNotificationMessage(payload);
            }
            
        } catch (Exception e) {
            log.error("Error processing Redis message", e);
        }
    }
    
    /**
     * 채팅 메시지를 처리합니다.
     * 
     * @param payload 메시지 페이로드
     */
    private void handleChatMessage(String payload) {
        try {
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
            
            // 개인 메시지인 경우
            if (chatMessage.getReceiverId() != null) {
                chatWebSocketHandler.sendMessageToUser(chatMessage.getReceiverId(), chatMessage);
            } 
            // 채팅방 메시지인 경우
            else if (chatMessage.getRoomId() != null) {
                // 채팅방의 모든 참여자에게 메시지 전송
                // 실제 구현에서는 채팅방 참여자 목록을 조회하여 전송
                broadcastToRoom(chatMessage);
            }
            
        } catch (Exception e) {
            log.error("Error handling chat message", e);
        }
    }
    
    /**
     * 알림 메시지를 처리합니다.
     * 
     * @param payload 메시지 페이로드
     */
    private void handleNotificationMessage(String payload) {
        try {
            NotificationMessage notification = objectMapper.readValue(payload, NotificationMessage.class);
            
            // 수신자에게 알림 전송
            if (notification.getReceiverId() != null) {
                chatWebSocketHandler.sendMessageToUser(notification.getReceiverId(), notification);
            }
            
        } catch (Exception e) {
            log.error("Error handling notification message", e);
        }
    }
    
    /**
     * 채팅방에 메시지를 브로드캐스트합니다.
     * 
     * @param message 채팅 메시지
     */
    private void broadcastToRoom(ChatMessage message) {
        // TODO: 실제 구현에서는 채팅방 참여자 목록을 조회하여 전송
        // 현재는 모든 활성 사용자에게 전송 (임시 구현)
        log.info("Broadcasting message to room {}: {}", message.getRoomId(), message.getContent());
        
        // 실제 구현 예시:
        // List<String> participants = getRoomParticipants(message.getRoomId());
        // participants.forEach(participantId -> 
        //     chatWebSocketHandler.sendMessageToUser(participantId, message));
    }
}

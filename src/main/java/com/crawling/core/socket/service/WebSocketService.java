package com.crawling.core.socket.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tspoon.core.socket.dto.*;
import com.tspoon.core.socket.handler.ChatWebSocketHandler;
import com.tspoon.core.socket.util.WebSocketUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 서비스
 * WebSocket 연결 관리 및 메시지 처리를 담당합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@Service
public class WebSocketService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic chatTopic;
    private final ChannelTopic notificationTopic;
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ObjectMapper objectMapper;
    
    public WebSocketService(
            @Qualifier("webSocketRedisTemplate") RedisTemplate<String, Object> redisTemplate,
            ChannelTopic chatTopic,
            ChannelTopic notificationTopic,
            @Lazy ChatWebSocketHandler chatWebSocketHandler,
            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.chatTopic = chatTopic;
        this.notificationTopic = notificationTopic;
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.objectMapper = objectMapper;
    }
    
    /**
     * 사용자별 활성 세션을 저장하는 맵
     * Key: 사용자 ID, Value: WebSocket 세션
     */
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();
    
    /**
     * 채팅방별 참여자를 저장하는 맵
     * Key: 채팅방 ID, Value: 참여자 ID Set
     */
    private final ConcurrentHashMap<String, Set<String>> roomParticipants = new ConcurrentHashMap<>();
    
    /**
     * 사용자 연결을 처리합니다.
     * 
     * @param userId 사용자 ID
     * @param session WebSocket 세션
     */
    public void handleUserConnect(String userId, WebSocketSession session) {
        activeUsers.add(userId);
        log.info("User {} connected. Active users: {}", userId, activeUsers.size());
        
        // 연결 알림 메시지 전송
        SystemMessage connectMessage = new SystemMessage();
        connectMessage.setContent("연결되었습니다.");
        connectMessage.setCode("CONNECTED");
        connectMessage.setSenderId(userId);
        
        chatWebSocketHandler.sendMessageToUser(userId, connectMessage);
    }
    
    /**
     * 사용자 연결 해제를 처리합니다.
     * 
     * @param userId 사용자 ID
     */
    public void handleUserDisconnect(String userId) {
        activeUsers.remove(userId);
        
        // 사용자가 참여한 모든 채팅방에서 제거
        roomParticipants.values().forEach(participants -> participants.remove(userId));
        
        log.info("User {} disconnected. Active users: {}", userId, activeUsers.size());
    }
    
    /**
     * WebSocket 메시지를 처리합니다.
     * 
     * @param message WebSocket 메시지
     * @param session WebSocket 세션
     */
    public void handleMessage(SocketMessage message, WebSocketSession session) {
        try {
            switch (message.getType()) {
                case CHAT:
                    handleChatMessage((ChatMessage) message);
                    break;
                case TYPING:
                    handleTypingMessage((TypingMessage) message);
                    break;
                case JOIN:
                    handleJoinMessage((JoinMessage) message);
                    break;
                case LEAVE:
                    handleLeaveMessage((LeaveMessage) message);
                    break;
                default:
                    log.warn("Unknown message type: {}", message.getType());
            }
        } catch (Exception e) {
            log.error("Error handling message", e);
        }
    }
    
    /**
     * 채팅 메시지를 처리합니다.
     * 
     * @param message 채팅 메시지
     */
    private void handleChatMessage(ChatMessage message) {
        if (!WebSocketUtils.isValidMessage(message.getContent())) {
            log.warn("Invalid chat message from user {}", message.getSenderId());
            return;
        }
        
        // Redis를 통해 메시지 브로드캐스트
        redisTemplate.convertAndSend(chatTopic.getTopic(), message);
        
        log.info("Chat message from {} to room {}: {}", 
                message.getSenderId(), message.getRoomId(), message.getContent());
    }
    
    /**
     * 타이핑 메시지를 처리합니다.
     * 
     * @param message 타이핑 메시지
     */
    private void handleTypingMessage(TypingMessage message) {
        // 채팅방의 다른 참여자들에게 타이핑 상태 전송
        Set<String> participants = roomParticipants.get(message.getRoomId());
        if (participants != null) {
            participants.stream()
                    .filter(userId -> !userId.equals(message.getSenderId()))
                    .forEach(userId -> chatWebSocketHandler.sendMessageToUser(userId, message));
        }
    }
    
    /**
     * 채팅방 참여 메시지를 처리합니다.
     * 
     * @param message 참여 메시지
     */
    private void handleJoinMessage(JoinMessage message) {
        String roomId = message.getRoomId();
        String userId = message.getSenderId();
        
        // 채팅방 참여자 목록에 추가
        roomParticipants.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        
        // 채팅방의 다른 참여자들에게 참여 알림 전송
        Set<String> participants = roomParticipants.get(roomId);
        if (participants != null) {
            participants.stream()
                    .filter(participantId -> !participantId.equals(userId))
                    .forEach(participantId -> chatWebSocketHandler.sendMessageToUser(participantId, message));
        }
        
        log.info("User {} joined room {}", userId, roomId);
    }
    
    /**
     * 채팅방 퇴장 메시지를 처리합니다.
     * 
     * @param message 퇴장 메시지
     */
    private void handleLeaveMessage(LeaveMessage message) {
        String roomId = message.getRoomId();
        String userId = message.getSenderId();
        
        // 채팅방 참여자 목록에서 제거
        Set<String> participants = roomParticipants.get(roomId);
        if (participants != null) {
            participants.remove(userId);
            
            // 채팅방의 다른 참여자들에게 퇴장 알림 전송
            participants.forEach(participantId -> 
                    chatWebSocketHandler.sendMessageToUser(participantId, message));
        }
        
        log.info("User {} left room {}", userId, roomId);
    }
    
    /**
     * 개인 메시지를 전송합니다.
     * 
     * @param senderId 발신자 ID
     * @param receiverId 수신자 ID
     * @param content 메시지 내용
     */
    public void sendPrivateMessage(String senderId, String receiverId, String content) {
        ChatMessage message = new ChatMessage(content, senderId, null);
        message.setReceiverId(receiverId);
        
        // 수신자가 온라인인 경우 직접 전송
        if (activeUsers.contains(receiverId)) {
            chatWebSocketHandler.sendMessageToUser(receiverId, message);
        } else {
            // 오프라인인 경우 Redis에 저장 (나중에 읽음 처리)
            redisTemplate.opsForList().leftPush("offline_messages:" + receiverId, message);
        }
    }
    
    /**
     * 채팅방 메시지를 전송합니다.
     * 
     * @param senderId 발신자 ID
     * @param roomId 채팅방 ID
     * @param content 메시지 내용
     */
    public void sendRoomMessage(String senderId, String roomId, String content) {
        ChatMessage message = new ChatMessage(content, senderId, roomId);
        
        // Redis를 통해 메시지 브로드캐스트
        redisTemplate.convertAndSend(chatTopic.getTopic(), message);
    }
    
    /**
     * 알림을 전송합니다.
     * 
     * @param receiverId 수신자 ID
     * @param title 알림 제목
     * @param content 알림 내용
     */
    public void sendNotification(String receiverId, String title, String content) {
        NotificationMessage notification = new NotificationMessage(title, content, receiverId);
        
        // Redis를 통해 알림 전송
        redisTemplate.convertAndSend(notificationTopic.getTopic(), notification);
    }
    
    /**
     * 현재 온라인 사용자 수를 반환합니다.
     * 
     * @return 온라인 사용자 수
     */
    public int getOnlineUserCount() {
        return activeUsers.size();
    }
    
    /**
     * 특정 사용자가 온라인인지 확인합니다.
     * 
     * @param userId 사용자 ID
     * @return 온라인 여부
     */
    public boolean isUserOnline(String userId) {
        return activeUsers.contains(userId);
    }
    
    /**
     * 채팅방 참여자 수를 반환합니다.
     * 
     * @param roomId 채팅방 ID
     * @return 참여자 수
     */
    public int getRoomParticipantCount(String roomId) {
        Set<String> participants = roomParticipants.get(roomId);
        return participants != null ? participants.size() : 0;
    }
}

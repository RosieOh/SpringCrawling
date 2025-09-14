package com.crawling.core.socket.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tspoon.core.socket.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * WebSocket 메시지 기본 클래스
 * 
 * @author tspoon
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ChatMessage.class, name = "CHAT"),
    @JsonSubTypes.Type(value = SystemMessage.class, name = "SYSTEM"),
    @JsonSubTypes.Type(value = NotificationMessage.class, name = "NOTIFICATION"),
    @JsonSubTypes.Type(value = TypingMessage.class, name = "TYPING"),
    @JsonSubTypes.Type(value = JoinMessage.class, name = "JOIN"),
    @JsonSubTypes.Type(value = LeaveMessage.class, name = "LEAVE")
})
public abstract class SocketMessage {
    
    /**
     * 메시지 타입
     */
    private MessageType type;
    
    /**
     * 메시지 ID
     */
    private String messageId;
    
    /**
     * 발신자 ID
     */
    protected String senderId;
    
    /**
     * 수신자 ID (개인 메시지인 경우)
     */
    protected String receiverId;
    
    /**
     * 채팅방 ID
     */
    protected String roomId;
    
    /**
     * 메시지 생성 시간
     */
    private LocalDateTime timestamp;
    
    /**
     * SocketMessage 생성자
     * 
     * @param type 메시지 타입
     */
    public SocketMessage(MessageType type) {
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.messageId = java.util.UUID.randomUUID().toString();
    }
}

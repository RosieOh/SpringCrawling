package com.crawling.core.socket.dto;

import com.tspoon.core.socket.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 타이핑 상태 메시지 DTO
 * 
 * @author tspoon
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TypingMessage extends SocketMessage {
    
    /**
     * 타이핑 상태 (START, STOP)
     */
    private String status;
    
    /**
     * 타이핑 중인 사용자 이름
     */
    private String userName;
    
    
    /**
     * TypingMessage 생성자
     * 
     * @param status 타이핑 상태
     * @param senderId 발신자 ID
     * @param roomId 채팅방 ID
     */
    public TypingMessage(String status, String senderId, String roomId) {
        super(MessageType.TYPING);
        this.status = status;
        this.senderId = senderId;
        this.roomId = roomId;
    }
}

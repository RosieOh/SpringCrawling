package com.crawling.core.socket.dto;

import com.tspoon.core.socket.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 채팅 메시지 DTO
 * 
 * @author tspoon
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatMessage extends SocketMessage {
    
    /**
     * 메시지 내용
     */
    private String content;
    
    /**
     * 메시지 타입 (TEXT, IMAGE, FILE 등)
     */
    private String messageType;
    
    /**
     * 첨부 파일 URL
     */
    private String attachmentUrl;
    
    /**
     * 답장 대상 메시지 ID
     */
    private String replyToMessageId;
    
    /**
     * 메시지 읽음 여부
     */
    private boolean isRead;
    
    
    /**
     * ChatMessage 생성자
     * 
     * @param content 메시지 내용
     * @param senderId 발신자 ID
     * @param roomId 채팅방 ID
     */
    public ChatMessage(String content, String senderId, String roomId) {
        super(MessageType.CHAT);
        this.content = content;
        this.senderId = senderId;
        this.roomId = roomId;
        this.messageType = "TEXT";
        this.isRead = false;
    }
}

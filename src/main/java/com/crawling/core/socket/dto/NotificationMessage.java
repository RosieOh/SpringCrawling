package com.crawling.core.socket.dto;

import com.tspoon.core.socket.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 알림 메시지 DTO
 * 
 * @author tspoon
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationMessage extends SocketMessage {
    
    /**
     * 알림 제목
     */
    private String title;
    
    /**
     * 알림 내용
     */
    private String content;
    
    /**
     * 알림 타입
     */
    private String notificationType;
    
    /**
     * 알림 우선순위
     */
    private String priority;
    
    /**
     * 클릭 시 이동할 URL
     */
    private String actionUrl;
    
    
    /**
     * NotificationMessage 생성자
     * 
     * @param title 알림 제목
     * @param content 알림 내용
     * @param receiverId 수신자 ID
     */
    public NotificationMessage(String title, String content, String receiverId) {
        super(MessageType.NOTIFICATION);
        this.title = title;
        this.content = content;
        this.receiverId = receiverId;
        this.notificationType = "GENERAL";
        this.priority = "NORMAL";
    }
}

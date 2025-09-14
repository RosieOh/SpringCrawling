package com.crawling.core.socket.dto;

import com.tspoon.core.socket.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 채팅방 퇴장 메시지 DTO
 * 
 * @author tspoon
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LeaveMessage extends SocketMessage {
    
    /**
     * 퇴장한 사용자 이름
     */
    private String userName;
    
    /**
     * 퇴장 사유
     */
    private String reason;
    
    
    /**
     * LeaveMessage 생성자
     * 
     * @param senderId 발신자 ID
     * @param roomId 채팅방 ID
     * @param userName 사용자 이름
     */
    public LeaveMessage(String senderId, String roomId, String userName) {
        super(MessageType.LEAVE);
        this.senderId = senderId;
        this.roomId = roomId;
        this.userName = userName;
    }
}

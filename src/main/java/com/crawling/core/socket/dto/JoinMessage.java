package com.crawling.core.socket.dto;

import com.tspoon.core.socket.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 채팅방 참여 메시지 DTO
 * 
 * @author tspoon
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class JoinMessage extends SocketMessage {
    
    /**
     * 참여한 사용자 이름
     */
    private String userName;
    
    /**
     * 참여한 사용자 프로필 이미지
     */
    private String profileImage;
    
    
    /**
     * JoinMessage 생성자
     * 
     * @param senderId 발신자 ID
     * @param roomId 채팅방 ID
     * @param userName 사용자 이름
     */
    public JoinMessage(String senderId, String roomId, String userName) {
        super(MessageType.JOIN);
        this.senderId = senderId;
        this.roomId = roomId;
        this.userName = userName;
    }
}

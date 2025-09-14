package com.crawling.core.socket.dto;

import com.tspoon.core.socket.enums.MessageType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 시스템 메시지 DTO
 * 
 * @author tspoon
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemMessage extends SocketMessage {
    
    /**
     * 시스템 메시지 내용
     */
    private String content;
    
    /**
     * 시스템 메시지 코드
     */
    private String code;
    
    /**
     * 추가 데이터
     */
    private Object data;
    
    
    /**
     * SystemMessage 생성자
     * 
     * @param content 메시지 내용
     * @param code 시스템 메시지 코드
     * @param roomId 채팅방 ID
     */
    public SystemMessage(String content, String code, String roomId) {
        super(MessageType.SYSTEM);
        this.content = content;
        this.code = code;
        this.roomId = roomId;
    }
}

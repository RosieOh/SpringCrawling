package com.crawling.core.socket.enums;

/**
 * WebSocket 메시지 타입을 정의하는 열거형
 * 
 * @author tspoon
 * @version 1.0
 */
public enum MessageType {
    
    /**
     * 채팅 메시지
     */
    CHAT("CHAT", "채팅 메시지"),
    
    /**
     * 시스템 메시지
     */
    SYSTEM("SYSTEM", "시스템 메시지"),
    
    /**
     * 알림 메시지
     */
    NOTIFICATION("NOTIFICATION", "알림 메시지"),
    
    /**
     * 타이핑 상태
     */
    TYPING("TYPING", "타이핑 상태"),
    
    /**
     * 채팅방 참여
     */
    JOIN("JOIN", "채팅방 참여"),
    
    /**
     * 채팅방 퇴장
     */
    LEAVE("LEAVE", "채팅방 퇴장"),
    
    /**
     * 연결 상태
     */
    CONNECT("CONNECT", "연결"),
    
    /**
     * 연결 해제
     */
    DISCONNECT("DISCONNECT", "연결 해제"),
    
    /**
     * 에러 메시지
     */
    ERROR("ERROR", "에러 메시지");
    
    /**
     * 메시지 타입 코드
     */
    private final String code;
    
    /**
     * 메시지 타입 설명
     */
    private final String description;
    
    /**
     * MessageType 생성자
     * 
     * @param code 메시지 타입 코드
     * @param description 메시지 타입 설명
     */
    MessageType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 메시지 타입 코드를 반환합니다.
     * 
     * @return 메시지 타입 코드
     */
    public String getCode() {
        return code;
    }
    
    /**
     * 메시지 타입 설명을 반환합니다.
     * 
     * @return 메시지 타입 설명
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 코드로 MessageType을 찾습니다.
     * 
     * @param code 메시지 타입 코드
     * @return MessageType 객체
     * @throws IllegalArgumentException 해당 코드의 메시지 타입이 없을 때
     */
    public static MessageType fromCode(String code) {
        for (MessageType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown message type code: " + code);
    }
}

package com.crawling.core.socket.util;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Map;

/**
 * WebSocket 유틸리티 클래스
 * 
 * @author tspoon
 * @version 1.0
 */
public class WebSocketUtils {
    
    /**
     * HTTP 요청에서 사용자 ID를 추출합니다.
     * 
     * @param request HTTP 요청
     * @return 사용자 ID
     */
    public static String extractUserIdFromRequest(ServerHttpRequest request) {
        URI uri = request.getURI();
        String query = uri.getQuery();
        
        if (StringUtils.hasText(query)) {
            Map<String, String> params = parseQueryString(query);
            return params.get("userId");
        }
        
        // Authorization 헤더에서 추출 (JWT 토큰 등)
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authHeader)) {
            return extractUserIdFromAuthHeader(authHeader);
        }
        
        return null;
    }
    
    /**
     * 쿼리 스트링을 파싱합니다.
     * 
     * @param query 쿼리 스트링
     * @return 파라미터 맵
     */
    private static Map<String, String> parseQueryString(String query) {
        Map<String, String> params = new java.util.HashMap<>();
        String[] pairs = query.split("&");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        
        return params;
    }
    
    /**
     * Authorization 헤더에서 사용자 ID를 추출합니다.
     * 
     * @param authHeader Authorization 헤더 값
     * @return 사용자 ID
     */
    private static String extractUserIdFromAuthHeader(String authHeader) {
        // TODO: JWT 토큰에서 사용자 ID 추출 로직 구현
        // 예: JWT 토큰 디코딩 후 사용자 ID 추출
        
        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // JWT 토큰 디코딩 로직 구현 필요
            return null; // 임시로 null 반환
        }
        
        return null;
    }
    
    /**
     * 채팅방 ID를 생성합니다.
     * 
     * @param userId1 사용자 ID 1
     * @param userId2 사용자 ID 2
     * @return 채팅방 ID
     */
    public static String generateRoomId(String userId1, String userId2) {
        // 사용자 ID를 정렬하여 일관된 채팅방 ID 생성
        if (userId1.compareTo(userId2) < 0) {
            return userId1 + "_" + userId2;
        } else {
            return userId2 + "_" + userId1;
        }
    }
    
    /**
     * 그룹 채팅방 ID를 생성합니다.
     * 
     * @param groupId 그룹 ID
     * @return 채팅방 ID
     */
    public static String generateGroupRoomId(String groupId) {
        return "group_" + groupId;
    }
    
    /**
     * 메시지가 유효한지 검증합니다.
     * 
     * @param content 메시지 내용
     * @return 유효성 여부
     */
    public static boolean isValidMessage(String content) {
        return StringUtils.hasText(content) && content.length() <= 1000; // 최대 1000자
    }
    
    /**
     * 사용자 ID가 유효한지 검증합니다.
     * 
     * @param userId 사용자 ID
     * @return 유효성 여부
     */
    public static boolean isValidUserId(String userId) {
        return StringUtils.hasText(userId) && userId.matches("^[a-zA-Z0-9_-]+$");
    }
}

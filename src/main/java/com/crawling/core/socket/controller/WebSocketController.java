package com.crawling.core.socket.controller;

import com.tspoon.core.socket.service.WebSocketService;
import com.tspoon.global.controller.BaseController;
import com.tspoon.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * WebSocket 관련 REST API 컨트롤러
 * WebSocket 연결 전 설정 및 상태 조회를 위한 API를 제공합니다.
 * 
 * @author tspoon
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping(BaseController.API_PATH + "/websocket")
@RequiredArgsConstructor
public class WebSocketController extends BaseController {
    
    private final WebSocketService webSocketService;
    
    /**
     * 개인 메시지를 전송합니다.
     * 
     * @param receiverId 수신자 ID
     * @param content 메시지 내용
     * @return 전송 결과
     */
    @PostMapping("/message/private")
    public ResponseEntity<ApiResponse<String>> sendPrivateMessage(
            @RequestParam String receiverId,
            @RequestParam String content) {
        
        try {
            // TODO: 실제 구현에서는 현재 사용자 ID를 세션에서 가져옴
            String senderId = "current_user_id";
            
            webSocketService.sendPrivateMessage(senderId, receiverId, content);
            
            return okMessage("메시지가 전송되었습니다.");
        } catch (Exception e) {
            log.error("Error sending private message", e);
            return errorString("MESSAGE_SEND_FAILED", "메시지 전송에 실패했습니다.");
        }
    }
    
    /**
     * 채팅방 메시지를 전송합니다.
     * 
     * @param roomId 채팅방 ID
     * @param content 메시지 내용
     * @return 전송 결과
     */
    @PostMapping("/message/room")
    public ResponseEntity<ApiResponse<String>> sendRoomMessage(
            @RequestParam String roomId,
            @RequestParam String content) {
        
        try {
            // TODO: 실제 구현에서는 현재 사용자 ID를 세션에서 가져옴
            String senderId = "current_user_id";
            
            webSocketService.sendRoomMessage(senderId, roomId, content);
            
            return okMessage("메시지가 전송되었습니다.");
        } catch (Exception e) {
            log.error("Error sending room message", e);
            return errorString("MESSAGE_SEND_FAILED", "메시지 전송에 실패했습니다.");
        }
    }
    
    /**
     * 알림을 전송합니다.
     * 
     * @param receiverId 수신자 ID
     * @param title 알림 제목
     * @param content 알림 내용
     * @return 전송 결과
     */
    @PostMapping("/notification")
    public ResponseEntity<ApiResponse<String>> sendNotification(
            @RequestParam String receiverId,
            @RequestParam String title,
            @RequestParam String content) {
        
        try {
            webSocketService.sendNotification(receiverId, title, content);
            
            return okMessage("알림이 전송되었습니다.");
        } catch (Exception e) {
            log.error("Error sending notification", e);
            return errorString("NOTIFICATION_SEND_FAILED", "알림 전송에 실패했습니다.");
        }
    }
    
    /**
     * 현재 온라인 사용자 수를 조회합니다.
     * 
     * @return 온라인 사용자 수
     */
    @GetMapping("/status/online-count")
    public ResponseEntity<ApiResponse<Integer>> getOnlineUserCount() {
        try {
            int count = webSocketService.getOnlineUserCount();
            return ok(count);
        } catch (Exception e) {
            log.error("Error getting online user count", e);
            return errorInteger("ONLINE_COUNT_FAILED", "온라인 사용자 수 조회에 실패했습니다.");
        }
    }
    
    /**
     * 특정 사용자의 온라인 상태를 확인합니다.
     * 
     * @param userId 사용자 ID
     * @return 온라인 여부
     */
    @GetMapping("/status/user/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> isUserOnline(@PathVariable String userId) {
        try {
            boolean isOnline = webSocketService.isUserOnline(userId);
            return ok(isOnline);
        } catch (Exception e) {
            log.error("Error checking user online status", e);
            return errorBoolean("USER_STATUS_FAILED", "사용자 온라인 상태 확인에 실패했습니다.");
        }
    }
    
    /**
     * 채팅방 참여자 수를 조회합니다.
     * 
     * @param roomId 채팅방 ID
     * @return 참여자 수
     */
    @GetMapping("/status/room/{roomId}/participants")
    public ResponseEntity<ApiResponse<Integer>> getRoomParticipantCount(@PathVariable String roomId) {
        try {
            int count = webSocketService.getRoomParticipantCount(roomId);
            return ok(count);
        } catch (Exception e) {
            log.error("Error getting room participant count", e);
            return errorInteger("ROOM_PARTICIPANT_FAILED", "채팅방 참여자 수 조회에 실패했습니다.");
        }
    }
}

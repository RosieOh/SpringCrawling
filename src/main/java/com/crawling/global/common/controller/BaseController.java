package com.crawling.global.common.controller;

import com.crawling.global.common.dto.ApiResponse;
import com.crawling.global.common.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    /**
     * 성공 응답 (데이터 포함)
     */
    protected <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    /**
     * 성공 응답 (메시지만)
     */
    protected ResponseEntity<ApiResponse<String>> okMessage(String message) {
        return ResponseEntity.ok(ApiResponse.ok(message));
    }

    /**
     * 생성 성공 응답
     */
    protected <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    /**
     * 실패 응답 (CommonException 기반)
     */
    protected ResponseEntity<ApiResponse<Void>> error(CommonException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.fail(e));
    }

    /**
     * 실패 응답 (커스텀 메시지, 상태코드 지정)
     */
    protected ResponseEntity<ApiResponse<Void>> error(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(status, false, null, null));
    }
}
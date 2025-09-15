package com.crawling.global.common.exception;

import com.crawling.global.common.enums.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommonException extends RuntimeException {
    private final ErrorCode errorCode;

    // 에러 발생시 ErrorCode 별 메시지
    @Override
    public String getMessage() {
        return this.errorCode.getMessage();
    }
}

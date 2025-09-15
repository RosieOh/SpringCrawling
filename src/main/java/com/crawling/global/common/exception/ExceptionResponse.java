package com.crawling.global.common.exception;

import com.crawling.global.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final Integer code;
    private final String message;

    public ExceptionResponse(ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public static ExceptionResponse of(ErrorCode errorCode){
        return new ExceptionResponse(errorCode);
    }

}
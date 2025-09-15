package com.crawling.global.common.dto;

import com.crawling.global.common.enums.ErrorCode;
import com.crawling.global.common.exception.CommonException;
import com.crawling.global.common.exception.ExceptionResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiResponse<T> {

    @JsonIgnore
    private HttpStatus status;

    @NotNull
    private boolean success;

    @Nullable
    private T result;

    @Nullable
    private ExceptionResponse error;

    // 성공 응답
    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>(
                HttpStatus.OK,
                true,
                result,
                null
        );
    }

    // 공통 예외 응답
    public static <T> ApiResponse<T> fail(CommonException e) {
        return new ApiResponse<>(
                e.getErrorCode().getHttpStatus(),
                false,
                null,
                ExceptionResponse.of(e.getErrorCode())
        );
    }

    // 400 - 요청 파라미터 누락
    public static <T> ApiResponse<T> fail(MissingServletRequestParameterException e) {
        return new ApiResponse<>(
                HttpStatus.BAD_REQUEST,
                false,
                null,
                ExceptionResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER)
        );
    }

    // 400 - 잘못된 파라미터 형식
    public static <T> ApiResponse<T> fail(MethodArgumentTypeMismatchException e) {
        return new ApiResponse<>(
                HttpStatus.BAD_REQUEST,
                false,
                null,
                ExceptionResponse.of(ErrorCode.INVALID_PARAMETER_FORMAT)
        );
    }
}

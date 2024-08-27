package com.devcci.devtoy.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "API 에러 응답")
public class ErrorResponse {

    @Schema(description = "응답시간")
    private final LocalDateTime timestamp = LocalDateTime.now();
    @Schema(description = "응답코드")
    private final int code;
    @Schema(description = "에러메시지")
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(new ErrorResponse(
                errorCode.getCode(),
                errorCode.getMessage()
            ));
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
            .status(errorCode.getStatus())
            .body(new ErrorResponse(
                errorCode.getCode(),
                message
            ));
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(int status, int errorCode, String message) {
        return ResponseEntity
            .status(status)
            .body(new ErrorResponse(
                errorCode,
                message
            ));
    }
}

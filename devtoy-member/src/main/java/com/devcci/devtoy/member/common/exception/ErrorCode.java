package com.devcci.devtoy.member.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 오류가 발생했습니다."),
    INVALID_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, 501, "포맷 에러"),
    ;

    ErrorCode(HttpStatus status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus status;
    private final int code;
    private final String message;
}

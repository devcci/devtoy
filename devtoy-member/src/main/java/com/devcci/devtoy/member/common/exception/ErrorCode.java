package com.devcci.devtoy.member.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 2500, "서버 오류가 발생했습니다."),
    INVALID_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, 2501, "포맷 에러"),

    MEMBER_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, 2001, "이미 등록된 사용자 ID 입니다."),
    MEMBER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, 2002, "이미 등록된 이메일입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, 2003, "존재하지 않는 사용자입니다."),
    MEMBER_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, 2004, "잘못된 패스워드입니다."),

    JWT_TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED, 9001, "인증 토큰이 존재하지 않습니다."),
    JWT_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, 9002, "잘못된 토큰입니다."),
    JWT_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, 9003, "만료된 토큰입니다."),
    JWT_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 9004, "토큰 발행 중 문제가 발생했습니다."),
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

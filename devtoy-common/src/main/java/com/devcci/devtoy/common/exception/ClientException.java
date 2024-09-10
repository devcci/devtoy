package com.devcci.devtoy.common.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private final ErrorCode errorCode;
    private String details;

    public ClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ClientException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = details;
    }
}

package com.devcci.devtoy.gateway.exception;

public class InternalApiException extends RuntimeException {
    private final int errorCode;
    private final int statusCode;

    public InternalApiException(int statusCode, int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
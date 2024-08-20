package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class NotFoundExceptionStrategy implements ExceptionStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof org.springframework.cloud.gateway.support.NotFoundException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        return ErrorResponse.toResponseEntity(ErrorCode.API_SERVER_NOT_AVAILABLE);
    }
}
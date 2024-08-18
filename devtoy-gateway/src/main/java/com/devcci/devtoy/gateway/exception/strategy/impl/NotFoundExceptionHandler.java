package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionHandlerStrategy;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class NotFoundExceptionHandler implements ExceptionHandlerStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof NotFoundException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        return ErrorResponse.toResponseEntity(ErrorCode.API_SERVER_NOT_AVAILABLE);
    }
}
package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.InternalApiException;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class InternalApiExceptionStrategy implements ExceptionStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof InternalApiException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        InternalApiException ex = (InternalApiException) e;
        return ErrorResponse.toResponseEntity(ex.getStatusCode(), ex.getErrorCode(), ex.getMessage());
    }
}

package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.resource.NoResourceFoundException;

@Component
public class ResponseStatusExceptionStrategy implements ExceptionStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof org.springframework.web.server.ResponseStatusException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        org.springframework.web.server.ResponseStatusException ex = (org.springframework.web.server.ResponseStatusException) e;
        if (e instanceof NoResourceFoundException) {
            return ErrorResponse.toResponseEntity(ErrorCode.NO_RESOURCE_FOUND);
        }
        return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, ex.getReason());
    }
}
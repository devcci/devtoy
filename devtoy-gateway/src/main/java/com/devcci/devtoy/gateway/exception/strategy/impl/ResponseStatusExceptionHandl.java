package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionHandlerStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ResponseStatusExceptionHandl implements ExceptionHandlerStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof ResponseStatusException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        ResponseStatusException ex = (ResponseStatusException) e;
        if (e instanceof NoResourceFoundException) {
            return ErrorResponse.toResponseEntity(ErrorCode.NO_RESOURCE_FOUND);
        }
        return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, ex.getReason());
    }
}
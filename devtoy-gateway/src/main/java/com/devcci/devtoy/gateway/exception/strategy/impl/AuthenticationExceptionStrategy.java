package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationExceptionStrategy implements ExceptionStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof com.devcci.devtoy.common.exception.AuthenticationException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        com.devcci.devtoy.common.exception.AuthenticationException ex = (com.devcci.devtoy.common.exception.AuthenticationException) e;
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }
}
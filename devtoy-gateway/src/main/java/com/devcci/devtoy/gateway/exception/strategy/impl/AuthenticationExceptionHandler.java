package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.AuthenticationException;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionHandlerStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationExceptionHandler implements ExceptionHandlerStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof AuthenticationException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        AuthenticationException ex = (AuthenticationException) e;
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }
}
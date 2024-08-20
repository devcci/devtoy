package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionStrategy;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class JwtExceptionStrategy implements ExceptionStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof io.jsonwebtoken.JwtException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        if (e instanceof ExpiredJwtException) {
            return ErrorResponse.toResponseEntity(ErrorCode.JWT_TOKEN_EXPIRED);
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.JWT_TOKEN_INVALID);
        }
    }
}
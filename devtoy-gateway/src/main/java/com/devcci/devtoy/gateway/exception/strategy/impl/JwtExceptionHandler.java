package com.devcci.devtoy.gateway.exception.strategy.impl;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionHandlerStrategy;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class JwtExceptionHandler implements ExceptionHandlerStrategy {

    @Override
    public boolean supports(Throwable e) {
        return e instanceof JwtException;
    }

    @Override
    public ResponseEntity<ErrorResponse> handle(Throwable e) {
        if (e instanceof ExpiredJwtException) {
            return ErrorResponse.toResponseEntity(ErrorCode.JWT_TOKEN_EXPIRED);
        } else if (e instanceof MalformedJwtException) {
            return ErrorResponse.toResponseEntity(ErrorCode.JWT_TOKEN_INVALID);
        } else {
            return ErrorResponse.toResponseEntity(ErrorCode.JWT_TOKEN_INVALID);
        }
    }
}
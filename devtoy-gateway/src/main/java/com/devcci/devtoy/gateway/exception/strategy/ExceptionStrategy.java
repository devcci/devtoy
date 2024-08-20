package com.devcci.devtoy.gateway.exception.strategy;

import com.devcci.devtoy.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;

public interface ExceptionStrategy {
    boolean supports(Throwable throwable);

    ResponseEntity<ErrorResponse> handle(Throwable throwable);
}

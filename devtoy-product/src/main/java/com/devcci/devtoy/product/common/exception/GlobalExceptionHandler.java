package com.devcci.devtoy.product.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
        MethodArgumentNotValidException.class)
    protected ResponseEntity<BindErrorResponse> handleBindException(
        MethodArgumentNotValidException exception) {
        return BindErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST,
            exception.getBindingResult());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<BindErrorResponse> constraintViolationException(
        ConstraintViolationException exception) {
        return BindErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST,
            exception.getConstraintViolations());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleException(ApiException exception) {
        return ErrorResponse.toResponseEntity(exception.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("error", exception);
        return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR,
            exception.getMessage());
    }
}

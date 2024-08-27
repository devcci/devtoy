package com.devcci.devtoy.common.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> noResourceFoundException(NoResourceFoundException exception) {
        return ErrorResponse.toResponseEntity(ErrorCode.NO_RESOURCE_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ErrorResponse.toResponseEntity(ErrorCode.METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(
        MethodArgumentNotValidException.class)
    public ResponseEntity<BindErrorResponse> handleBindException(
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

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse> missingRequestHeaderException(
        MissingRequestHeaderException exception) {
        return ErrorResponse.toResponseEntity(ErrorCode.MISSING_REQUEST_HEADER);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> authenticationException(AuthenticationException exception) {
        return ErrorResponse.toResponseEntity(exception.getErrorCode());
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> clientException(ClientException exception) {
        return ErrorResponse.toResponseEntity(exception.getErrorCode(), exception.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleException(ApiException exception) {
        return ErrorResponse.toResponseEntity(exception.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("error message: {}", exception.getMessage(), exception);
        return ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}

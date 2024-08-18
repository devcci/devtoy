package com.devcci.devtoy.gateway.exception;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.strategy.ExceptionHandlerStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;


@Order(-1)
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;
    private final List<ExceptionHandlerStrategy> exceptionHandlerStrategies;

    public GlobalExceptionHandler(ObjectMapper objectMapper, List<ExceptionHandlerStrategy> exceptionHandlerStrategies) {
        this.objectMapper = objectMapper;
        this.exceptionHandlerStrategies = exceptionHandlerStrategies;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        for (ExceptionHandlerStrategy strategy : exceptionHandlerStrategies) {
            if (strategy.supports(ex)) {
                return handleException(exchange, response, strategy, ex);
            }
        }

        ResponseEntity<ErrorResponse> defaultErrorResponse = ErrorResponse.toResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
        return writeErrorResponse(response, defaultErrorResponse);
    }

    private Mono<Void> handleException(ServerWebExchange exchange, ServerHttpResponse response, ExceptionHandlerStrategy strategy, Throwable e) {
        ResponseEntity<ErrorResponse> errorResponse = strategy.handle(e);
        return writeErrorResponse(response, errorResponse);
    }

    private Mono<Void> writeErrorResponse(ServerHttpResponse response, ResponseEntity<ErrorResponse> errorResponse) {
        String jsonErrorResponse;
        try {
            response.setStatusCode(errorResponse.getStatusCode());
            jsonErrorResponse = objectMapper.writeValueAsString(errorResponse.getBody());
        } catch (JsonProcessingException ex) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            jsonErrorResponse = ex.getMessage();
        }

        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonErrorResponse.getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}

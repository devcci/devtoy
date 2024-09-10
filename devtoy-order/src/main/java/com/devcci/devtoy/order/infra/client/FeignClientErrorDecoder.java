package com.devcci.devtoy.order.infra.client;

import com.devcci.devtoy.common.exception.ClientException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

// eureka - scg를 통해 로드밸런싱 적용된 경우 FeignClient에서 Retryer를 설정하지 않아도 로드밸런서의 retry가 수행된다.
// 이때 FeignClient의 ErrorDecoder 전에 RetryableException이 발생하기때문에 ErrorDecoder가 동작하지 않는다.
// cloud.loadbalancer.retry.enabled= false
// 위와 같이 적용해줘야 FeignClient의 ErrorDecoder를 사용할 수 있다.
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String errorMessage = "Unknown error";

        try {
            if (response.body() != null) {
                InputStream bodyStream = response.body().asInputStream();
                byte[] bytes = bodyStream.readAllBytes();
                String body = new String(bytes, StandardCharsets.UTF_8);

                if (body.startsWith("{")) {
                    JsonNode errorResponse = objectMapper.readTree(body);
                    errorMessage = errorResponse.path("message").asText("Unknown error");
                } else {
                    errorMessage = body;
                }
            }
        } catch (Exception e) {
            errorMessage = "Error Message Parsing Error";
        }

        return switch (response.status()) {
            case 400 -> new ClientException(ErrorCode.INVALID_HTTP_REQUEST, errorMessage);
            case 404 -> new ClientException(ErrorCode.HTTP_CLIENT_NOT_FOUND, errorMessage);
            case 503 -> new ClientException(ErrorCode.HTTP_CLIENT_SERVICE_UNAVAILABLE, errorMessage);
            default -> new ClientException(ErrorCode.HTTP_CLIENT_ERROR, errorMessage);
        };
    }
}

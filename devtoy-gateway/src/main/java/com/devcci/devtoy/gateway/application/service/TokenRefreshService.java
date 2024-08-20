package com.devcci.devtoy.gateway.application.service;

import com.devcci.devtoy.common.exception.ErrorResponse;
import com.devcci.devtoy.gateway.exception.InternalApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TokenRefreshService {
    private final WebClient.Builder webClientBuilder;
    private final String memberServiceUri;

    public TokenRefreshService(
        WebClient.Builder webClientBuilder, @Value("${service.uri.member}") String memberServiceUri
    ) {
        this.webClientBuilder = webClientBuilder;
        this.memberServiceUri = memberServiceUri;
    }

    public Mono<String> refresh(String memberId) {
        return webClientBuilder
            .baseUrl(memberServiceUri)
            .build()
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/auth/refresh")
                .queryParam("memberId", memberId)
                .build()
            )
            .exchangeToMono(clientResponse -> {
                HttpStatusCode status = clientResponse.statusCode();
                Mono<String> response = clientResponse.bodyToMono(String.class);
                if (status.is2xxSuccessful()) {
                    return response;
                } else {
                    return clientResponse.bodyToMono(ErrorResponse.class)
                        .flatMap(errorResponse -> {
                            return Mono.error(new InternalApiException(
                                status.value(),
                                errorResponse.getCode(),
                                errorResponse.getMessage()));
                        });
                }
            });
    }
}

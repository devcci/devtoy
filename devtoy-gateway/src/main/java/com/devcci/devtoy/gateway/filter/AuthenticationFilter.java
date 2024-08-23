package com.devcci.devtoy.gateway.filter;

import com.devcci.devtoy.common.Constans.AuthConstants;
import com.devcci.devtoy.common.exception.AuthenticationException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final SecretKey secretKey;

    public AuthenticationFilter(
        @Value("${jwt.secret-key}") String secretKey) {
        super(Config.class);
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public static class Config {
        private List<String> roles;

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            if (
                path.startsWith("/auth/signup") ||
                    path.startsWith("/auth/login") ||
                    path.startsWith("/auth/refresh")
            ) {
                return chain.filter(exchange);
            }
            ServerHttpRequest request = exchange.getRequest();
            log.info("request uri : {}", request.getURI());
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                throw new AuthenticationException(ErrorCode.JWT_TOKEN_NOT_EXISTS);
            String token = parse(request);
            Claims payload = verifyAndGetPayload(token);
            checkMemberRole(payload, config.getRoles());
            return chain.filter(exchange);
        };
    }

    private void checkMemberRole(Claims payload, List<String> requiredRole) {
        List roles = Optional.of(payload.get(AuthConstants.ROLES, List.class))
            .orElseThrow(() -> new AuthenticationException(ErrorCode.JWT_TOKEN_INVALID));

        boolean hasRequiredRole = roles.stream().anyMatch(requiredRole::contains);

        if (!hasRequiredRole) {
            throw new AuthenticationException(ErrorCode.NOT_AUTHORIZED);
        }
    }

    private Claims verifyAndGetPayload(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private String parse(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return TokenUtils.extractBearerToken(header);
    }
}

package com.devcci.devtoy.gateway.filter;

import com.devcci.devtoy.common.Constans.AuthConstants;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.gateway.exception.AuthenticationException;
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
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final SecretKey secretKey;

    public AuthenticationFilter(
        @Value("${jwt.secret-key}") String secretKey
    ) {
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
            if (path.startsWith("/auth")) {
                return chain.filter(exchange);
            }
            ServerHttpRequest request = exchange.getRequest();
            log.info("request uri : {}", request.getURI());
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                throw new AuthenticationException(ErrorCode.JWT_TOKEN_NOT_EXISTS);
            String token = parse(request);
            Claims payload = verifyAndGetPayload(token);
            List<String> requiredRole = config.getRoles();

            List roles = Optional.of(payload.get(AuthConstants.ROLES, List.class))
                .orElseThrow(() -> new AuthenticationException(ErrorCode.JWT_TOKEN_INVALID));

            boolean hasRequiredRole = roles.stream().anyMatch(requiredRole::contains);

            if (!hasRequiredRole) {
                throw new AuthenticationException(ErrorCode.NOT_AUTHORIZED);
            }
            return chain.filter(exchange);
        };
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
        if (StringUtils.hasText(header) && header.startsWith(AuthConstants.BEARER)) {
            return header.substring(7);
        }
        throw new AuthenticationException(ErrorCode.JWT_TOKEN_INVALID);
    }
}

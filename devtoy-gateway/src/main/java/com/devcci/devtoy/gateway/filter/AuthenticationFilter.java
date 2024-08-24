package com.devcci.devtoy.gateway.filter;

import com.devcci.devtoy.common.constans.AuthConstants;
import com.devcci.devtoy.common.constans.DevtoyHeaders;
import com.devcci.devtoy.common.exception.AuthenticationException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.gateway.util.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

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
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                throw new AuthenticationException(ErrorCode.JWT_TOKEN_NOT_EXISTS);
            String token = parse(request);
            Claims payload = verifyAndGetPayload(token);
            List<String> roles = getMemberRole(payload);
            checkMemberRole(roles, config.getRoles());
            String memberId = getMemberId(payload);
            String rolesString = String.join(",", roles);
            
            exchange = exchange.mutate()
                .request(r -> r
                    .header(DevtoyHeaders.MEMBER_ID, memberId)
                    .header(DevtoyHeaders.MEMBER_ROLES, rolesString))
                .build();
            return chain.filter(exchange);
        };
    }

    private String getMemberId(Claims payload) {
        return payload.getSubject();
    }

    private List<String> getMemberRole(Claims payload) {
        List<?> rawRoles = Optional.of(payload.get(AuthConstants.ROLES, List.class))
            .orElseThrow(() -> new AuthenticationException(ErrorCode.JWT_TOKEN_INVALID));
        return rawRoles.stream()
            .filter(role -> role instanceof String)
            .map(role -> (String) role)
            .collect(Collectors.toList());
    }

    private void checkMemberRole(List<String> roles, List<String> requiredRole) {
        boolean hasRequiredRole = roles.stream()
            .anyMatch(role -> requiredRole != null && requiredRole.contains(role));
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

package com.devcci.devtoy.member.infra.jwt;

import com.devcci.devtoy.member.common.exception.ErrorCode;
import com.devcci.devtoy.member.infra.jwt.auth.AuthConstants;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parse(request);

        try {
            if (token != null && !jwtProvider.isExpired(token)) {
                SecurityContextHolder.getContext()
                    .setAuthentication(jwtProvider.getAuthentication(token));
            }
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
                 SignatureException | UsernameNotFoundException e) {
            request.setAttribute(AuthConstants.REQUEST_ATTR_EXCEPTION, ErrorCode.JWT_TOKEN_INVALID.name());
        } catch (ExpiredJwtException e) {
            request.setAttribute(AuthConstants.REQUEST_ATTR_EXCEPTION, ErrorCode.JWT_TOKEN_EXPIRED.name());
        }

        filterChain.doFilter(request, response);
    }

    private String parse(HttpServletRequest request) {
        String header = request.getHeader(AuthConstants.AUTHORIZATIONR);
        if (StringUtils.hasText(header) && header.startsWith(AuthConstants.BEARER)) {
            return header.substring(7);
        }
        return null;
    }
}

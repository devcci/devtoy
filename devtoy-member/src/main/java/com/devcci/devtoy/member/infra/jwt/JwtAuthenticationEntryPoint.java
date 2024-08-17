package com.devcci.devtoy.member.infra.jwt;

import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.exception.JwtErrorResponse;
import com.devcci.devtoy.member.infra.jwt.auth.AuthConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ApplicationEventPublisher eventPublisher;

    public JwtAuthenticationEntryPoint(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute(AuthConstants.REQUEST_ATTR_EXCEPTION);

        if (exception == null) {
            setResponse(response, ErrorCode.JWT_TOKEN_NOT_EXISTS);
        } else if (exception.equals(ErrorCode.JWT_TOKEN_INVALID.name())) {
            setResponse(response, ErrorCode.JWT_TOKEN_INVALID);
        } else if (exception.equals(ErrorCode.JWT_TOKEN_EXPIRED.name())) {
            setResponse(response, ErrorCode.JWT_TOKEN_EXPIRED);
        }
//        eventPublisher.publishEvent(new JwtDeletionEvent(request.get))
    }

    private void setResponse(HttpServletResponse response, ErrorCode code) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset-UTF-8");
        response.setStatus(code.getStatus().value());
        JwtErrorResponse jwtErrorResponse = JwtErrorResponse.of(code.getCode(), code.getMessage());
        String result = new ObjectMapper().writeValueAsString(jwtErrorResponse);
        response.getWriter().write(result);
    }

}

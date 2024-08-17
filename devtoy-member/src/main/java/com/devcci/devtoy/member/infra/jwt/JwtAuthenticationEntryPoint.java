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

//https://yookeun.github.io/java/2023/12/30/jwt-refresh/
//https://colabear754.tistory.com/179
//https://velog.io/@sheltonwon/MSA-API-Gateway-%ED%8C%A8%ED%84%B4%EC%97%90%EC%84%9C%EC%9D%98-%EC%9D%B8%EC%A6%9D%EC%9D%B8%EA%B0%80

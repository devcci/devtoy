package com.devcci.devtoy.gateway.util;

import com.devcci.devtoy.common.constans.AuthConstants;
import com.devcci.devtoy.common.exception.AuthenticationException;
import com.devcci.devtoy.common.exception.ErrorCode;
import org.springframework.util.StringUtils;

public class TokenUtils {
    private TokenUtils() {
    }

    public static String extractBearerToken(String header) {
        if (!StringUtils.hasText(header) || !header.startsWith(AuthConstants.BEARER))
            throw new AuthenticationException(ErrorCode.JWT_TOKEN_INVALID);
        String token = header.substring(AuthConstants.BEARER.length());
        if (token.equals("null"))
            throw new AuthenticationException(ErrorCode.JWT_TOKEN_INVALID);
        return token;
    }
}

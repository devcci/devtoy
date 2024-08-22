package com.devcci.devtoy.common.util;

import com.devcci.devtoy.common.exception.AuthenticationException;
import com.devcci.devtoy.common.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenUtilsTest {

    @Test
    void extractBearerToken() {
        // given
        String validHeader = "Bearer validToken";

        // when
        String token = TokenUtils.extractBearerToken(validHeader);

        // then
        assertThat(token).isEqualTo("validToken");
    }

    @Test
    void extractBearerTokenWithoutBearer() {
        // given
        String invalidHeader = "InvalidHeader";

        // when & then
        assertThatThrownBy(() -> TokenUtils.extractBearerToken(invalidHeader))
            .isInstanceOf(AuthenticationException.class)
            .hasMessageContaining(ErrorCode.JWT_TOKEN_INVALID.getMessage());
    }

    @Test
    void extractBearerTokenEmptyHeader() {
        // given
        String emptyHeader = "";

        // when & then
        assertThatThrownBy(() -> TokenUtils.extractBearerToken(emptyHeader))
            .isInstanceOf(AuthenticationException.class)
            .hasMessageContaining(ErrorCode.JWT_TOKEN_INVALID.getMessage());
    }

    @Test
    void extractBearerTokenNullHeader() {
        // given
        String nullHeader = null;

        // when & then
        assertThatThrownBy(() -> TokenUtils.extractBearerToken(nullHeader))
            .isInstanceOf(AuthenticationException.class)
            .hasMessageContaining(ErrorCode.JWT_TOKEN_INVALID.getMessage());
    }
}
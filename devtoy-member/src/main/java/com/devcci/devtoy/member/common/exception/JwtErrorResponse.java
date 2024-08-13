package com.devcci.devtoy.member.common.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "JWT 에러 응답")
public class JwtErrorResponse {
    @Schema(description = "응답코드")
    private final int code;
    @Schema(description = "에러메시지")
    private final String message;

    public static JwtErrorResponse of(int code, String message) {
        return new JwtErrorResponse(
            code,
            message
        );
    }
}

package com.devcci.devtoy.member.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "사용자 로그인 정보 DTO")
public class LoginResponse {
    @Schema(description = "사용자 AccessToken")
    private String accessToken;

    private LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public static LoginResponse of(String accessToken) {
        return new LoginResponse(
            accessToken
        );
    }
}

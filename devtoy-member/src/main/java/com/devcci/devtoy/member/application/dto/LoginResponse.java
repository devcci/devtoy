package com.devcci.devtoy.member.application.dto;

import com.devcci.devtoy.member.domain.member.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "사용자 로그인 정보 DTO")
public class LoginResponse {
    private String name;
    private Set<String> role;
    @Schema(description = "사용자 AccessToken")
    private String accessToken;
    @Schema(description = "사용자 RefreshToken")
    private String refreshToken;

    private LoginResponse(String name, Set<String> role, String accessToken, String refreshToken) {
        this.name = name;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponse of(String name, Set<MemberRole> role, String accessToken, String refreshToken) {
        return new LoginResponse(
            name,
            role.stream().map(MemberRole::getValue).collect(Collectors.toSet()),
            accessToken,
            refreshToken);
    }
}

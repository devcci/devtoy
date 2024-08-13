package com.devcci.devtoy.member.infra.cache.redis.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberJwtInfo {
    private String memberId;
    private String accessToken;
    private String refreshToken;

    private MemberJwtInfo(String memberId, String accessToken, String refreshToken) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static MemberJwtInfo createMemberJwtInfo(String memberId, String accessToken, String refreshToken) {
        return new MemberJwtInfo(memberId, accessToken, refreshToken);
    }
}

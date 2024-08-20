package com.devcci.devtoy.member.infra.cache.redis.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberRefreshToken {
    private String memberId;
    private String refreshToken;

    private MemberRefreshToken(String memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public static MemberRefreshToken createMemberJwtInfo(String memberId, String refreshToken) {
        return new MemberRefreshToken(memberId, refreshToken);
    }
}

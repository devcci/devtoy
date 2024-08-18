package com.devcci.devtoy.member.infra.cache.redis.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberJwtInfo {
    private String memberId;
    private String refreshToken;

    private MemberJwtInfo(String memberId, String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }

    public static MemberJwtInfo createMemberJwtInfo(String memberId, String refreshToken) {
        return new MemberJwtInfo(memberId, refreshToken);
    }
}

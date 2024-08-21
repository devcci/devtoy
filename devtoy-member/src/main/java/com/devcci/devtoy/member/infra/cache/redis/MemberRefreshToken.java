package com.devcci.devtoy.member.infra.cache.redis;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@RedisHash("member:refresh-token")
public class MemberRefreshToken {
    @Id
    private String memberId;
    private String refreshToken;
    @TimeToLive(unit = TimeUnit.HOURS)
    private Long expireTime;

    private MemberRefreshToken(String memberId, String refreshToken, Long expireTime) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    public static MemberRefreshToken createMemberJwtInfo(
        String memberId,
        String refreshToken,
        Long expireTime

    ) {
        return new MemberRefreshToken(memberId, refreshToken, expireTime);
    }
}

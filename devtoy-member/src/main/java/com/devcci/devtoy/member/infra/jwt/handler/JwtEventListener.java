package com.devcci.devtoy.member.infra.jwt.handler;


import com.devcci.devtoy.member.infra.cache.redis.JwtRedisRepository;
import com.devcci.devtoy.member.infra.cache.redis.MemberRefreshToken;
import com.devcci.devtoy.member.infra.jwt.event.JwtAdditionEvent;
import com.devcci.devtoy.member.infra.jwt.event.JwtDeletionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class JwtEventListener {

    private final JwtRedisRepository jwtRedisRepository;
    private final Long refreshExpireTimeHour;

    public JwtEventListener(
        JwtRedisRepository jwtRedisRepository,
        @Value("${jwt.refresh-expire-time-hour}") Long refreshExpireTimeHour
    ) {
        this.jwtRedisRepository = jwtRedisRepository;
        this.refreshExpireTimeHour = refreshExpireTimeHour;
    }


    @Async("asyncExecutor")
    @EventListener
    public void onJwtCreated(JwtAdditionEvent event) {
        jwtRedisRepository.save
            (MemberRefreshToken.createMemberJwtInfo(
                event.memberId(),
                event.refreshToken(),
                refreshExpireTimeHour)
            );
    }

    @Async("asyncExecutor")
    @EventListener
    public void onJwtDeleted(JwtDeletionEvent event) {
        jwtRedisRepository.deleteById(event.memberId());
    }
}

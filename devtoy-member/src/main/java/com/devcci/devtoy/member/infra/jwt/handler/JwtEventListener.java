package com.devcci.devtoy.member.infra.jwt.handler;


import com.devcci.devtoy.common.infra.redis.RedisKey;
import com.devcci.devtoy.common.infra.redis.RedisTemplateService;
import com.devcci.devtoy.member.infra.jwt.event.JwtAdditionEvent;
import com.devcci.devtoy.member.infra.jwt.event.JwtDeletionEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class JwtEventListener {

    private final RedisTemplateService redisTemplateService;
    private final Long refreshExpireTimeHour;

    public JwtEventListener(
        RedisTemplateService redisTemplateService,
        @Value("${jwt.refresh-expire-time-hour}") Long refreshExpireTimeHour
    ) {
        this.redisTemplateService = redisTemplateService;
        this.refreshExpireTimeHour = refreshExpireTimeHour;
    }


    @Async("asyncExecutor")
    @EventListener
    public void onJwtCreated(JwtAdditionEvent event) {
        String key = RedisKey.REFRESH_TOKEN.generate(event.memberId());
        redisTemplateService.save(key, event.refreshToken(), refreshExpireTimeHour, TimeUnit.HOURS);
    }

    @Async("asyncExecutor")
    @EventListener
    public void onJwtDeleted(JwtDeletionEvent event) {
        String key = RedisKey.REFRESH_TOKEN.generate(event.memberId());
        redisTemplateService.delete(key);
    }
}

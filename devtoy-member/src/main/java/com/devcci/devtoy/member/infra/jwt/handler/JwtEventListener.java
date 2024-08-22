package com.devcci.devtoy.member.infra.jwt.handler;


import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.member.infra.cache.redis.RedisKeyPrefix;
import com.devcci.devtoy.member.infra.cache.redis.RedisTemplateService;
import com.devcci.devtoy.member.infra.jwt.event.JwtAdditionEvent;
import com.devcci.devtoy.member.infra.jwt.event.JwtDeletionEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        String key = RedisKeyPrefix.REFRESH_TOKEN.generateKey(event.memberId());
        try {
            redisTemplateService.save(key, event.refreshToken(), refreshExpireTimeHour, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorCode.JWT_CREATION_FAILED);
        }
    }

    @Async("asyncExecutor")
    @EventListener
    public void onJwtDeleted(JwtDeletionEvent event) {
        String key = RedisKeyPrefix.REFRESH_TOKEN.generateKey(event.memberId());
        redisTemplateService.delete(key);
    }
}

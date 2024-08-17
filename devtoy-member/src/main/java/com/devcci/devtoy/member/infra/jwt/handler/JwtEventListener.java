package com.devcci.devtoy.member.infra.jwt.handler;


import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.member.infra.cache.redis.JwtRedisRepository;
import com.devcci.devtoy.member.infra.cache.redis.dto.MemberJwtInfo;
import com.devcci.devtoy.member.infra.jwt.event.JwtAdditionEvent;
import com.devcci.devtoy.member.infra.jwt.event.JwtDeletionEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JwtEventListener {

    private final JwtRedisRepository jwtRedisRepository;

    public JwtEventListener(
        JwtRedisRepository jwtRedisRepository) {
        this.jwtRedisRepository = jwtRedisRepository;
    }


    @EventListener
    public void onJwtCreated(JwtAdditionEvent event) {
        try {
            jwtRedisRepository.saveRedis(MemberJwtInfo.createMemberJwtInfo(event.memberId(), event.accessToken(), event.refreshToken()));
        } catch (JsonProcessingException e) {
            throw new ApiException(ErrorCode.JWT_CREATION_FAILED);
        }
    }

    @EventListener
    public void onJwtDeleted(JwtDeletionEvent event) {
        jwtRedisRepository.deleteRedis(event.memberId());
    }
}

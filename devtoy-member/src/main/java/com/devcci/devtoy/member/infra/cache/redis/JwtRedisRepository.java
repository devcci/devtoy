package com.devcci.devtoy.member.infra.cache.redis;

import com.devcci.devtoy.member.infra.cache.redis.dto.MemberRefreshToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class JwtRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Long refreshExpireTimeHour;

    public JwtRedisRepository(
        RedisTemplate<String, Object> redisTemplate,
        @Value("${jwt.refresh-expire-time-hour}") Long refreshExpireTimeHour
    ) {
        this.redisTemplate = redisTemplate;
        this.refreshExpireTimeHour = refreshExpireTimeHour;
    }

    public void saveRedis(MemberRefreshToken memberRefreshToken) throws JsonProcessingException {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        ObjectMapper objectMapper = new ObjectMapper();
        valueOperations.set(memberRefreshToken.getMemberId(), objectMapper.writeValueAsString(memberRefreshToken));
        redisTemplate.expire(memberRefreshToken.getMemberId(), refreshExpireTimeHour, TimeUnit.HOURS);
    }

    public void deleteRedis(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(key);
    }

    public boolean existsRefreshTokenById(String userId) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String json = (String) valueOperations.get(userId);
        return !StringUtils.isEmpty(json);
    }
}

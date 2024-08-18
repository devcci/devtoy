package com.devcci.devtoy.member.infra.cache.redis;

import com.devcci.devtoy.member.infra.cache.redis.dto.MemberJwtInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;
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

    public void saveRedis(MemberJwtInfo memberJwtInfo) throws JsonProcessingException {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        ObjectMapper objectMapper = new ObjectMapper();
        valueOperations.set(memberJwtInfo.getMemberId(), objectMapper.writeValueAsString(memberJwtInfo));
        redisTemplate.expire(memberJwtInfo.getMemberId(), refreshExpireTimeHour, TimeUnit.HOURS);
    }

    public void deleteRedis(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(key);
    }

    private Optional<MemberJwtInfo> findRefreshTokenById(String userId)
        throws JsonProcessingException {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        String json = (String) valueOperations.get(userId);

        if (StringUtils.isEmpty(json)) {
            return Optional.empty();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return Optional.of(objectMapper.readValue(json, MemberJwtInfo.class));

    }
}

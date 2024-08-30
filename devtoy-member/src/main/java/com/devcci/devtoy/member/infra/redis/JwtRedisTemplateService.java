package com.devcci.devtoy.member.infra.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class JwtRedisTemplateService {

    private final RedisTemplate<String, String> jwtRedisTemplate;

    public JwtRedisTemplateService(RedisTemplate<String, String> jwtRedisTemplate) {
        this.jwtRedisTemplate = jwtRedisTemplate;
    }

    public void save(String key, String value, Long expireTime, TimeUnit timeUnit) {
        jwtRedisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    public void delete(String key) {
        jwtRedisTemplate.delete(key);
    }

    public String get(String key) {
        return jwtRedisTemplate.opsForValue().get(key);
    }

}

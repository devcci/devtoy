package com.devcci.devtoy.common.infra.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisTemplateService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisTemplateService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void saveProductViewCount(Object value) {
        redisTemplate.opsForZSet().incrementScore(RedisKey.PRODUCT_VIEW_COUNT.getKey(), value, 1);
    }

    public Double getProductViewCount(Object value) {
        return redisTemplate.opsForZSet().score(RedisKey.PRODUCT_VIEW_COUNT.getKey(), value);
    }

    public Set<Object> getTopProducts(int limit) {
        return redisTemplate.opsForZSet().reverseRange(RedisKey.PRODUCT_VIEW_COUNT.getKey(), 0, limit - 1);
    }

    public void resetProductViewCount() {
        redisTemplate.delete(RedisKey.PRODUCT_VIEW_COUNT.getKey());
    }
}

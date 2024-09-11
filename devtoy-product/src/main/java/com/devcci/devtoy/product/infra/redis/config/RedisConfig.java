package com.devcci.devtoy.product.infra.redis.config;

import com.devcci.devtoy.common.infra.redis.config.CommonRedisConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    private final CommonRedisConfig commonRedisConfig;
    private final RedisConnectionFactory redisConnectionFactory;

    public RedisConfig(CommonRedisConfig commonRedisConfig,
        RedisConnectionFactory redisConnectionFactory) {
        this.commonRedisConfig = commonRedisConfig;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Long> productViewRedisTemplate() {
        return commonRedisConfig.createRedisTemplate(redisConnectionFactory, Long.class);
    }
}

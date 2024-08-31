package com.devcci.devtoy.common.infra.redis;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum RedisKey {
    REFRESH_TOKEN("refreshToken", 24L, TimeUnit.HOURS),
    PRODUCT_INFO("product:info", 6L, TimeUnit.HOURS),
    PRODUCT_VIEW_COUNT("product:viewCount", null, TimeUnit.HOURS);

    private final String key;
    private final Long expireTime;
    private final TimeUnit timeUnit;

    RedisKey(String key, Long expireTime, TimeUnit timeUnit) {
        this.key = key;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
    }

    public String generate(String id) {
        return key + ":" + id;
    }
}
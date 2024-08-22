package com.devcci.devtoy.member.infra.cache.redis;

import lombok.Getter;

@Getter
public enum RedisKeyPrefix {
    REFRESH_TOKEN("refreshToken");

    private final String prefix;

    RedisKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String generateKey(String suffix) {
        return prefix + "::" + suffix;
    }
}
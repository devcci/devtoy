package com.devcci.devtoy.product.infra.cache.local;

import com.devcci.devtoy.product.infra.cache.CacheProps;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(CacheProps.values())
            .map(cache -> new CaffeineCache(cache.getName(), Caffeine.newBuilder().recordStats()
                .expireAfterWrite(cache.getExpiredTime(), TimeUnit.MINUTES)
                .maximumSize(cache.getMaximumSize())
                .build()))
            .toList();
    }

    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caches = caffeineCaches();
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}

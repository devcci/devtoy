package com.devcci.devtoy.product.infra.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, @Nullable Object key) {
        log.error("Cache get failed: {}, cache: {}, key: {}", exception.getMessage(), cache.getName(), key);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, @Nullable Object key, Object value) {
        log.error("Cache put failed: {}, cache: {}, key: {}, value: {}", exception.getMessage(), cache.getName(), key,
            value);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, @Nullable Object key) {
        log.error("Cache evict failed: {}, cache: {}, key: {}", exception.getMessage(), cache.getName(), key);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("Cache clear failed: {}, cache: {}", exception.getMessage(), cache.getName());
    }
}

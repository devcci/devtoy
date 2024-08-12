package com.devcci.devtoy.product.infra.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheRefreshHandler {

    public CacheRefreshHandler(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    CacheManager cacheManager;

    @Caching(evict = {
        @CacheEvict(value = "lowestPriceBrand", allEntries = true),
        @CacheEvict(value = "lowestPriceCategory", allEntries = true),
        @CacheEvict(value = "categoryMinMaxPrice", allEntries = true)
    })
    public void refreshProductCache() {
        cacheManager.getCacheNames().forEach(name -> log.info("Refresh cache: {}", name));
    }
}

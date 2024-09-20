package com.devcci.devtoy.product.infra.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheRefreshHandler {

    @Caching(evict = {
        @CacheEvict(value = "lowestPriceBrand", allEntries = true),
        @CacheEvict(value = "lowestPriceCategory", allEntries = true),
        @CacheEvict(value = "categoryMinMaxPrice", allEntries = true),
        @CacheEvict(value = "productInfoList", allEntries = true),
    })
    public void clearProductCacheEntries() {
        log.info("Clearing all product-related cache entries.");
    }

    @CacheEvict(value = "productInfo", key = "#p0")
    public void evictProductInfoCache(Long ignoredProductId) {
        log.info("Evicting cache for productInfo with productId: {}", ignoredProductId);

    }
}

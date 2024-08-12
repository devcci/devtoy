package com.devcci.devtoy.product.infra.cache;

import lombok.Getter;

@Getter
public enum CacheProps {
    LOWEST_PRICE_CATEGORY("lowestPriceCategory", 5, 1000),
    LOWEST_PRICE_BRAND("lowestPriceBrand", 5, 1000),
    CATEGORY_MIN_MAX_PRICE("categoryMinMaxPrice", 5, 1000);

    private final String name;
    private final int expiredTime;
    private final int maximumSize;

    CacheProps(String name, int expiredTime, int maximumSize) {
        this.name = name;
        this.expiredTime = expiredTime;
        this.maximumSize = maximumSize;
    }
}


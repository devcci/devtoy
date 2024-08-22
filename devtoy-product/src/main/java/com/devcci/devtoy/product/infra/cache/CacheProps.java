package com.devcci.devtoy.product.infra.cache;

import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import lombok.Getter;

@Getter
public enum CacheProps {
    LOWEST_PRICE_CATEGORY("lowestPriceCategory", 10, LowestPriceCategoryResponse.class),
    LOWEST_PRICE_BRAND("lowestPriceBrand", 10, LowestPriceBrandProductsResponse.class),
    CATEGORY_MIN_MAX_PRICE("categoryMinMaxPrice", 10, CategoryPriceRangeResponse.class);

    private final String name;
    private final int expiredTime;
    private final Class<?> type;

    CacheProps(String name, int expiredTime, Class<?> type) {
        this.name = name;
        this.expiredTime = expiredTime;
        this.type = type;
    }
}


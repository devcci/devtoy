package com.devcci.devtoy.product.infra.cache;

import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import com.devcci.devtoy.product.application.dto.ProductInfo;
import com.devcci.devtoy.product.application.dto.ProductInfos;
import lombok.Getter;

import java.time.Duration;

@Getter
public enum CacheProps {
    LOWEST_PRICE_CATEGORY("lowestPriceCategory", 360, LowestPriceCategoryResponse.class),
    LOWEST_PRICE_BRAND("lowestPriceBrand", 360, LowestPriceBrandProductsResponse.class),
    CATEGORY_MIN_MAX_PRICE("categoryMinMaxPrice", 360, CategoryPriceRangeResponse.class),
    PRODUCT_INFO_LIST("productInfoList", 360, ProductInfos.class),
    PRODUCT_INFO("productInfo", 360, ProductInfo.class);

    private final String name;
    private final Duration expiredTime;
    private final Class<?> type;

    CacheProps(String name, int expiredTime, Class<?> type) {
        this.name = name;
        this.expiredTime = Duration.ofMinutes(expiredTime);
        this.type = type;
    }
}


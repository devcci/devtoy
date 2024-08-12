package com.devcci.devtoy.product.infra.persistence.projection;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LowestProductByBrandProjection {

    private final String brandName;
    private final String categoryName;
    private final BigDecimal productPrice;

    public LowestProductByBrandProjection(String brandName, String categoryName,
                                          BigDecimal productPrice) {
        this.brandName = brandName;
        this.categoryName = categoryName;
        this.productPrice = productPrice;
    }
}

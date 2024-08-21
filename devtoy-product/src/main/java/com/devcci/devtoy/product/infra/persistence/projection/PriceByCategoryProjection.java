package com.devcci.devtoy.product.infra.persistence.projection;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PriceByCategoryProjection {

    private final String productName;
    private final String brandName;
    private final BigDecimal productPrice;

    public PriceByCategoryProjection(String productName, String brandName, BigDecimal productPrice) {
        this.productName = productName;
        this.brandName = brandName;
        this.productPrice = productPrice;
    }
}

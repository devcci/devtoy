package com.devcci.devtoy.product.infra.persistence.projection;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PriceByCategoryProjection {

    private final String brandName;
    private final BigDecimal productPrice;

    public PriceByCategoryProjection(String brandName, BigDecimal productPrice) {
        this.brandName = brandName;
        this.productPrice = productPrice;
    }
}

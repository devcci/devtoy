package com.devcci.devtoy.product.infra.persistence.projection;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LowestProductByCategoryProjection {

    private final String productName;
    private final String categoryName;
    private final String brandName;
    private final BigDecimal productPrice;


    public LowestProductByCategoryProjection(String productName, String categoryName, String brandName,
                                             BigDecimal productPrice) {
        this.productName = productName;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.productPrice = productPrice;
    }
}

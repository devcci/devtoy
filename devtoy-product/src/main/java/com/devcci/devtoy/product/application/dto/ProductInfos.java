package com.devcci.devtoy.product.application.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductInfos {

    private List<ProductInfo> productInfos = new ArrayList<>();

    private ProductInfos(List<ProductInfo> productInfos) {
        this.productInfos = productInfos;
    }

    public static ProductInfos createProductInfos(List<ProductInfo> productInfos) {
        return new ProductInfos(productInfos);
    }
}

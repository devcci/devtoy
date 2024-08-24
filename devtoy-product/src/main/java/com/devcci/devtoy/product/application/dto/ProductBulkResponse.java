package com.devcci.devtoy.product.application.dto;

import com.devcci.devtoy.product.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductBulkResponse {

    private Long productId;
    private BigDecimal price;
    private Long stockQuantity;

    private ProductBulkResponse(Long productId,
                                BigDecimal price,
                                Long stockQuantity) {
        this.productId = productId;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static ProductBulkResponse of(Product product) {
        return new ProductBulkResponse(
            product.getId(),
            product.getPrice(),
            product.getStockQuantity());
    }
}

package com.devcci.devtoy.product.application.dto;

import com.devcci.devtoy.product.common.util.NumberFormatUtil;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.category.Category;
import com.devcci.devtoy.product.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductResponse {

    private Long productId;
    private String productName;
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private String price;
    private Long stockQuantity;

    private ProductResponse(Long productId,
                            String productName,
                            Long brandId,
                            String brandName,
                            Long categoryId,
                            String categoryName,
                            String price,
                            Long stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.brandId = brandId;
        this.brandName = brandName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public static ProductResponse of(Product product) {
        Brand brand = product.getBrand();
        Category category = product.getCategory();
        return new ProductResponse(
            product.getId(),
            product.getName(),
            brand.getId(),
            brand.getName(),
            category.getId(),
            category.getName(),
            NumberFormatUtil.withComma(product.getPrice()),
            product.getStockQuantity());
    }
}

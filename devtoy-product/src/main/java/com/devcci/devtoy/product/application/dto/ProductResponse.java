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
    private Long brandId;
    private String brandName;
    private Long categoryId;
    private String categoryName;
    private String price;

    private ProductResponse(Long productId, Long brandId, String brandName, Long categoryId,
                            String categoryName, String price) {
        this.productId = productId;
        this.brandId = brandId;
        this.brandName = brandName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
    }

    public static ProductResponse of(Product product) {
        Brand brand = product.getBrand();
        Category category = product.getCategory();
        return new ProductResponse(
            product.getId(),
            brand.getId(),
            brand.getName(),
            category.getId(),
            category.getName(),
            NumberFormatUtil.withComma(product.getPrice()));
    }
}

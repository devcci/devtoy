package com.devcci.devtoy.product.application.dto;

import com.devcci.devtoy.product.common.util.NumberFormatUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LowestPriceCategoryResponse {

    private List<CategoryProduct> categoryProducts;
    @JsonProperty("총액")
    private String totalPrice;

    private LowestPriceCategoryResponse(List<CategoryProduct> categoryProducts,
                                        String totalPrice) {

        this.categoryProducts = categoryProducts;
        this.totalPrice = totalPrice;
    }

    public static LowestPriceCategoryResponse createLowestPriceBrandCategory(
        List<CategoryProduct> categoryProduct, BigDecimal totalPrice) {
        return new LowestPriceCategoryResponse(categoryProduct,
            NumberFormatUtil.withComma(totalPrice));
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class CategoryProduct {

        @JsonProperty("상품명")
        private String productName;
        @JsonProperty("카테고리")
        private String categoryName;
        @JsonProperty("브랜드")
        private String brandName;
        @JsonProperty("가격")
        private String price;

        private CategoryProduct(String productName, String categoryName, String brandName, String price) {
            this.productName = productName;
            this.categoryName = categoryName;
            this.brandName = brandName;
            this.price = price;
        }

        public static CategoryProduct createCategoryProduct(
            String productName,
            String categoryName,
            String brandName,
            BigDecimal price
        ) {
            return new CategoryProduct(productName, categoryName, brandName, NumberFormatUtil.withComma(price));
        }
    }
}

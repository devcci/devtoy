package com.devcci.devtoy.product.application.dto;

import com.devcci.devtoy.product.common.util.NumberFormatUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CategoryPriceRangeResponse {

    @JsonProperty("카테고리")
    private String categoryName;
    @JsonProperty("최저가")
    private LowestPriceProduct lowestPriceProduct;
    @JsonProperty("최고가")
    private HighestPriceProduct highestPriceProduct;

    private CategoryPriceRangeResponse(
        String categoryName, LowestPriceProduct lowestPriceProduct,
        HighestPriceProduct highestPriceProduct) {
        this.categoryName = categoryName;
        this.lowestPriceProduct = lowestPriceProduct;
        this.highestPriceProduct = highestPriceProduct;
    }

    public static CategoryPriceRangeResponse createCategoryPriceRangeResponse(
        String categoryName,
        LowestPriceProduct lowestPriceProduct,
        HighestPriceProduct highestPriceProduct) {
        return new CategoryPriceRangeResponse(categoryName, lowestPriceProduct,
            highestPriceProduct);
    }


    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class LowestPriceProduct {

        @JsonProperty("상품명")
        private String productName;
        @JsonProperty("브랜드")
        private String brandName;
        @JsonProperty("가격")
        private String price;

        private LowestPriceProduct(String productName, String brandName, String price) {
            this.productName = productName;
            this.brandName = brandName;
            this.price = price;
        }

        public static LowestPriceProduct createLowestPriceProduct(String productName, String brandName, BigDecimal price) {
            return new LowestPriceProduct(productName, brandName, NumberFormatUtil.withComma(price));
        }
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class HighestPriceProduct {

        @JsonProperty("상품명")
        private String productName;
        @JsonProperty("브랜드")
        private String brandName;
        @JsonProperty("가격")
        private String price;

        private HighestPriceProduct(String productName, String brandName, String price) {
            this.productName = productName;
            this.brandName = brandName;
            this.price = price;
        }

        public static HighestPriceProduct createHighestPriceProduct(String productName, String brandName, BigDecimal price) {
            return new HighestPriceProduct(productName, brandName, NumberFormatUtil.withComma(price));
        }
    }
}

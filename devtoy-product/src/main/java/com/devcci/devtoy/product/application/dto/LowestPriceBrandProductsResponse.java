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
public class LowestPriceBrandProductsResponse {

    @JsonProperty("최저가")
    private LowestPriceBrandProduct lowestPriceBrandProduct;

    private LowestPriceBrandProductsResponse(LowestPriceBrandProduct lowestPriceBrandProduct) {
        this.lowestPriceBrandProduct = lowestPriceBrandProduct;
    }

    public static LowestPriceBrandProductsResponse createLowestPriceBrandProductsResponse(
        LowestPriceBrandProduct lowestPriceBrandProduct) {
        return new LowestPriceBrandProductsResponse(lowestPriceBrandProduct);
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class LowestPriceBrandProduct {

        @JsonProperty("브랜드")
        private String brandName;
        @JsonProperty("카테고리")
        private List<BrandProduct> brandProducts;
        @JsonProperty("총액")
        private String totalPrice;

        private LowestPriceBrandProduct(String brandName, List<BrandProduct> brandProducts,
                                        String totalPrice) {
            this.brandName = brandName;
            this.brandProducts = brandProducts;
            this.totalPrice = totalPrice;
        }

        public static LowestPriceBrandProduct createLowestPriceBrandProducts(String brandName,
                                                                             List<BrandProduct> categoryProducts,
                                                                             BigDecimal totalPrice) {
            return new LowestPriceBrandProduct(brandName, categoryProducts,
                NumberFormatUtil.withComma(totalPrice));
        }

        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @Getter
        public static class BrandProduct {

            @JsonProperty("상품명")
            private String productName;
            @JsonProperty("카테고리")
            private String categoryName;
            @JsonProperty("가격")
            private String price;

            private BrandProduct(String productName, String categoryName, String price) {
                this.productName = productName;
                this.categoryName = categoryName;
                this.price = price;
            }

            public static BrandProduct createBrandProduct(String productName, String categoryName, BigDecimal price) {
                return new BrandProduct(productName, categoryName, NumberFormatUtil.withComma(price));
            }
        }
    }
}

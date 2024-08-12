package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse.LowestPriceBrandProduct.BrandProduct;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse.CategoryProduct;
import com.devcci.devtoy.product.application.dto.ProductResponse;
import com.devcci.devtoy.product.config.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
class ProductSearchServiceIntegrationTest {

    @Autowired
    private ProductSearchService productSearchService;

    @DisplayName("성공 - 상품 목록 조회")
    @Test
    void findAllProductSuccess() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "id"));

        // when
        List<ProductResponse> productResponses = productSearchService.findAllProduct(pageable);

        // then
        assertThat(productResponses).hasSize(10);
        ProductResponse product = productResponses.get(0);
        assertThat(product.getCategoryName()).isEqualTo("상의");
        assertThat(product.getBrandName()).isEqualTo("A");
        assertThat(product.getPrice()).isEqualTo("11,200");
    }

    @DisplayName("성공 - 카테고리별 최저가 상품")
    @Test
    void getLowestPriceProductPerCategorySuccess() {
        // when
        LowestPriceCategoryResponse response = productSearchService.getLowestPriceProductPerCategory();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getTotalPrice()).isEqualTo("34,100");
        List<CategoryProduct> categoryProducts = response.getCategoryProducts();
        assertThat(categoryProducts).hasSize(8);
        assertThat(categoryProducts.get(0).getCategoryName()).isEqualTo("가방");
        assertThat(categoryProducts.get(0).getBrandName()).isEqualTo("A");
        assertThat(categoryProducts.get(0).getPrice()).isEqualTo("2,000");
        assertThat(categoryProducts.get(1).getCategoryName()).isEqualTo("모자");
        assertThat(categoryProducts.get(1).getBrandName()).isEqualTo("D");
        assertThat(categoryProducts.get(1).getPrice()).isEqualTo("1,500");
    }

    @DisplayName("성공 - 최저가 브랜드의 카테고리별 상품")
    @Test
    void getLowestPriceProductByBrandSuccess() {
        // when
        LowestPriceBrandProductsResponse response = productSearchService.getLowestPriceProductByBrand();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getLowestPriceBrandProduct().getBrandName()).isEqualTo(
            "D");
        assertThat(response.getLowestPriceBrandProduct().getTotalPrice()).isEqualTo(
            "36,100");

        List<BrandProduct> brandProducts = response.getLowestPriceBrandProduct()
            .getBrandProducts();
        assertThat(brandProducts).hasSize(8);
        assertThat(brandProducts.get(0).getCategoryName()).isEqualTo("상의");
        assertThat(brandProducts.get(0).getPrice()).isEqualTo("10,100");
    }

    @DisplayName("성공 - 카테고리의 최고가, 최저가 상품")
    @Test
    void getCategoryMinMaxPricesSuccess() {
        // given
        String categoryName = "바지";

        // when
        CategoryPriceRangeResponse response = productSearchService.getCategoryMinMaxPrices(
            categoryName);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        assertThat(response.getLowestPriceProduct().getBrandName()).isEqualTo("D");
        assertThat(response.getLowestPriceProduct().getPrice()).isEqualTo("3,000");
        assertThat(response.getHighestPriceProduct().getBrandName()).isEqualTo("A");
        assertThat(response.getHighestPriceProduct().getPrice()).isEqualTo("4,200");
    }
}
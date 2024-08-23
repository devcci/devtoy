package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse.LowestPriceBrandProduct.BrandProduct;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import com.devcci.devtoy.product.application.dto.ProductResponse;
import com.devcci.devtoy.product.config.UnitTest;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.category.Category;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByBrandProjection;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByCategoryProjection;
import com.devcci.devtoy.product.infra.persistence.projection.PriceByCategoryProjection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@UnitTest
class ProductSearchServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductSearchService productSearchService;

    @DisplayName("성공 - 상품 목록 조회")
    @Test
    void findAllProduct() {
        // given
        Brand brand1 = Brand.createBrand("brand1");
        Brand brand2 = Brand.createBrand("brand2");
        Category category1 = Category.createCategory("category1");
        Category category2 = Category.createCategory("category2");
        Product product1 = Product.createProduct("상품1", new BigDecimal(1000), brand1, category1, "상품", 1L);
        Product product2 = Product.createProduct("상품2", new BigDecimal(1000), brand2, category2, "상품2", 1L);
        List<Product> products = List.of(product1, product2);
        given(productRepository.findAllFetchJoin(any(Pageable.class))).willReturn(products);

        // when
        List<ProductResponse> productResponses = productSearchService.findAllProduct(
            Pageable.unpaged());

        // then
        verify(productRepository, times(1)).findAllFetchJoin(any(Pageable.class));
        assertThat(productResponses).hasSize(2);
        assertThat(productResponses.get(0).getBrandName()).isEqualTo(
            products.get(0).getBrand().getName());
        assertThat(productResponses.get(0).getCategoryName()).isEqualTo(
            products.get(0).getCategory().getName());
        assertThat(productResponses.get(1).getBrandName()).isEqualTo(
            products.get(1).getBrand().getName());
        assertThat(productResponses.get(1).getCategoryName()).isEqualTo(
            products.get(1).getCategory().getName());
    }

    @DisplayName("실패 - 상품 목록 조회")
    @Test
    void findAllProductNotFoundList() {
        // given
        given(productRepository.findAllFetchJoin(any(Pageable.class))).willReturn(
            Collections.emptyList());

        // when
        Throwable throwable = catchThrowable(() -> productSearchService.findAllProduct(
            Pageable.unpaged()));

        // then
        verify(productRepository, times(1)).findAllFetchJoin(any(Pageable.class));
        assertThat(throwable)
            .isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.PRODUCT_LIST_NOT_LOADED.getMessage());
    }

    @DisplayName("성공 - 카테고리별 최저가 상품")
    @Test
    void getLowestPriceProductPerCategory() {
        // given
        LowestProductByCategoryProjection product1 = mock(
            LowestProductByCategoryProjection.class);
        LowestProductByCategoryProjection product2 = mock(
            LowestProductByCategoryProjection.class);

        given(product1.getCategoryName()).willReturn("Category1");
        given(product1.getBrandName()).willReturn("Brand1");
        given(product1.getProductPrice()).willReturn(new BigDecimal(100));

        given(product2.getCategoryName()).willReturn("Category2");
        given(product2.getBrandName()).willReturn("Brand2");
        given(product2.getProductPrice()).willReturn(new BigDecimal(150));

        List<LowestProductByCategoryProjection> productProjections = List.of(product1,
            product2);
        given(productRepository.findLowestPriceProductByCategory()).willReturn(
            productProjections);

        // when
        LowestPriceCategoryResponse response = productSearchService.getLowestPriceProductPerCategory();

        // then
        assertThat(response).isNotNull();
        assertThat(Long.valueOf(response.getTotalPrice())).isEqualTo(250L);

        List<LowestPriceCategoryResponse.CategoryProduct> categoryProducts = response.getCategoryProducts();
        assertThat(categoryProducts).hasSize(2);
        assertThat(categoryProducts.get(0).getCategoryName()).isEqualTo("Category1");
        assertThat(categoryProducts.get(0).getBrandName()).isEqualTo("Brand1");
        assertThat(Long.valueOf(categoryProducts.get(0).getPrice())).isEqualTo(100L);
        assertThat(categoryProducts.get(1).getCategoryName()).isEqualTo("Category2");
        assertThat(categoryProducts.get(1).getBrandName()).isEqualTo("Brand2");
        assertThat(Long.valueOf(categoryProducts.get(1).getPrice())).isEqualTo(150L);
        verify(productRepository, times(1)).findLowestPriceProductByCategory();
    }

    @DisplayName("실패 - 카테고리별 최저가 상품이 없음")
    @Test
    void getLowestPriceProductPerCategoryEmptyList() {
        // given
        given(productRepository.findLowestPriceProductByCategory())
            .willReturn(Collections.emptyList());

        // when
        Throwable thrown = catchThrowable(
            () -> productSearchService.getLowestPriceProductPerCategory());

        // then
        assertThat(thrown)
            .isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.CATEGORY_LOWEST_PRICE_PRODUCT_ERROR.getMessage());

        verify(productRepository, times(1)).findLowestPriceProductByCategory();
    }

    @DisplayName("성공 - 최저가 브랜드의 카테고리별 상품")
    @Test
    void getLowestPriceProductByBrand() {
        // given
        LowestProductByBrandProjection product1 = mock(LowestProductByBrandProjection.class);
        LowestProductByBrandProjection product2 = mock(LowestProductByBrandProjection.class);
        given(product1.getBrandName()).willReturn("Brand1");
        given(product1.getCategoryName()).willReturn("Category1");
        given(product1.getProductPrice()).willReturn(new BigDecimal(100));
        given(product2.getBrandName()).willReturn("Brand1");
        given(product2.getCategoryName()).willReturn("Category2");
        given(product2.getProductPrice()).willReturn(new BigDecimal(150));

        List<LowestProductByBrandProjection> productProjections = List.of(product1, product2);
        given(productRepository.findLowestPriceProductByBrand())
            .willReturn(productProjections);

        // when
        LowestPriceBrandProductsResponse response = productSearchService.getLowestPriceProductByBrand();

        // then
        assertThat(response).isNotNull();
        assertThat(response.getLowestPriceBrandProduct().getBrandName())
            .isEqualTo("Brand1");
        assertThat(Long.valueOf(response.getLowestPriceBrandProduct().getTotalPrice()))
            .isEqualTo(250L);

        List<BrandProduct> categoryProducts = response.getLowestPriceBrandProduct()
            .getBrandProducts();
        assertThat(categoryProducts).hasSize(2);
        assertThat(categoryProducts.get(0).getCategoryName())
            .isEqualTo(product1.getCategoryName());
        assertThat(new BigDecimal(categoryProducts.get(0).getPrice()))
            .isEqualTo(product1.getProductPrice());
        assertThat(categoryProducts.get(1).getCategoryName())
            .isEqualTo(product2.getCategoryName());
        assertThat(new BigDecimal(categoryProducts.get(1).getPrice()))
            .isEqualTo(product2.getProductPrice());
        verify(productRepository, times(1)).findLowestPriceProductByBrand();
    }

    @DisplayName("실패 - 최저가 브랜드의 카테고리별 상품 목록이 없음")
    @Test
    void getLowestPriceProductByBrand_EmptyList() {
        // given
        given(productRepository.findLowestPriceProductByBrand()).willReturn(
            Collections.emptyList());

        // when
        Throwable throwable = catchThrowable(
            () -> productSearchService.getLowestPriceProductByBrand());

        // then
        assertThat(throwable).isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.BRAND_LOWEST_PRICE_LIST_ERROR.getMessage());
    }

    @DisplayName("성공 - 카테고리의 최고가, 최저가 상품")
    @Test
    void getCategoryMinMaxPrices() {
        // given
        String categoryName = "Category1";
        PriceByCategoryProjection lowestPriceProjection =
            mock(PriceByCategoryProjection.class);
        PriceByCategoryProjection highestPriceProjection =
            mock(PriceByCategoryProjection.class);
        given(productRepository.findLowestPriceByCategory(categoryName)).willReturn(
            Optional.of(lowestPriceProjection));
        given(productRepository.findHighestPriceByCategory(categoryName)).willReturn(
            Optional.of(highestPriceProjection));
        given(lowestPriceProjection.getBrandName()).willReturn("Brand1");
        given(lowestPriceProjection.getProductPrice()).willReturn(new BigDecimal("100"));
        given(highestPriceProjection.getBrandName()).willReturn("Brand2");
        given(highestPriceProjection.getProductPrice()).willReturn(new BigDecimal("500"));

        // when
        CategoryPriceRangeResponse response = productSearchService.getCategoryMinMaxPrices(
            categoryName);

        // then
        verify(productRepository, times(1)).findLowestPriceByCategory(categoryName);
        verify(productRepository, times(1)).findHighestPriceByCategory(categoryName);
        assertThat(response).isNotNull();
        assertThat(response.getCategoryName()).isEqualTo(categoryName);
        assertThat(response.getLowestPriceProduct().getBrandName())
            .isEqualTo(lowestPriceProjection.getBrandName());
        assertThat(new BigDecimal(response.getLowestPriceProduct().getPrice()))
            .isEqualTo(lowestPriceProjection.getProductPrice());
        assertThat(response.getHighestPriceProduct().getBrandName())
            .isEqualTo(highestPriceProjection.getBrandName());
        assertThat(new BigDecimal(response.getHighestPriceProduct().getPrice()))
            .isEqualTo(highestPriceProjection.getProductPrice());
    }

    @DisplayName("실패 - 카테고리의 최저가 상품이 존재하지 않음")
    @Test
    void getCategoryMinMaxPricesThrowsExceptionForLowestPrice() {
        // given
        String categoryName = "Category1";
        given(productRepository.findLowestPriceByCategory(categoryName)).willReturn(
            Optional.empty());

        // when
        Throwable thrown = catchThrowable(
            () -> productSearchService.getCategoryMinMaxPrices(categoryName));

        // then
        assertThat(thrown).isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.CATEGORY_LOWEST_PRICE_PRODUCT_ERROR.getMessage());
    }

    @DisplayName("실패 - 카테고리의 최고가 상품이 존재하지 않음")
    @Test
    void getCategoryMinMaxPricesThrowsExceptionForHighestPrice() {
        // given
        String categoryName = "Category1";
        PriceByCategoryProjection lowestPriceProjection = new PriceByCategoryProjection("상품", "brand", new BigDecimal(2000));

        given(productRepository.findLowestPriceByCategory(categoryName)).willReturn(
            Optional.of(lowestPriceProjection));
        given(productRepository.findHighestPriceByCategory(categoryName)).willReturn(
            Optional.empty());
        // when
        Throwable thrown = catchThrowable(
            () -> productSearchService.getCategoryMinMaxPrices(categoryName));

        // then
        assertThat(thrown).isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.CATEGORY_HIGHEST_PRICE_PRODUCT_ERROR.getMessage());
    }
}

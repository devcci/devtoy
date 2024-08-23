package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.config.IntegrationTest;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.web.dto.ProductAddRequest;
import com.devcci.devtoy.product.web.dto.ProductUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class ProductManageServiceIntegrationTest {

    @Autowired
    ProductManageService productManageService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @DisplayName("상품 추가")
    @Nested
    class ProductAdd {

        @DisplayName("성공")
        @Test
        void addProductSuccess() {
            // given
            Brand brand = Brand.createBrand("newBrand");
            brandRepository.save(brand);
            String productName = "상품";
            String brandName = "newBrand";
            String categoryName = "상의";
            BigDecimal price = new BigDecimal(1000);
            String description = "상품";
            ProductAddRequest productAddRequest =
                new ProductAddRequest(productName, brandName, categoryName, price, description, 1L);

            // then
            Product result = productManageService.addProduct(productAddRequest);

            // when
            Product product = productRepository.findById(result.getId()).orElseThrow();
            assertThat(result.getId()).isEqualTo(product.getId());
            assertThat(product.getName()).isEqualTo(productName);
            assertThat(result.getBrand().getName()).isEqualTo(brandName);
            assertThat(result.getCategory().getName()).isEqualTo(categoryName);
            assertThat(result.getPrice()).isEqualTo(price);
            assertThat(result.getDescription()).isEqualTo(description);
            assertThat(result.getStockQuantity()).isEqualTo(1L);
        }


        @DisplayName("실패 - 브랜드의 카테고리에 이미 상품이 존재")
        @Test
        void addProductDuplicateThrowException() {
            // given
            ProductAddRequest productAddRequest;
            productAddRequest = new ProductAddRequest("상품", "A", "상의", new BigDecimal(1000), "상품", 1L);

            // when
            Throwable throwable = catchThrowable(
                () -> productManageService.addProduct(productAddRequest));

            // then
            assertThat(throwable).isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.PRODUCT_DUPLICATE.getMessage());
        }

        @DisplayName("실패 - 존재하지 않는 브랜드")
        @Test
        void addProductBrandNotFoundThrowException() {
            // given
            ProductAddRequest productAddRequest = new ProductAddRequest("상품", "Q", "상의",
                new BigDecimal(1000), "Q", 1L);

            // when
            Throwable throwable = catchThrowable(
                () -> productManageService.addProduct(productAddRequest));

            // then
            assertThat(throwable).isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_FOUND.getMessage());
        }

        @DisplayName("실패 - 존재하지 않는 카테고리")
        @Test
        void addProductCategoryNotFoundThrowException() {
            // given
            ProductAddRequest productAddRequest = new ProductAddRequest("상품", "A", "마마", new BigDecimal(1000), "상품", 1L);

            // when
            Throwable throwable = catchThrowable(
                () -> productManageService.addProduct(productAddRequest));

            // then
            assertThat(throwable).isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
        }
    }


    @DisplayName("상품 수정")
    @Nested
    class ProductUpdate {

        @DisplayName("실패 - 변경 내용 없음")
        @Test
        void updateProductNotChangedThrowException() {
            // given
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "상품", "A", "상의",
                new BigDecimal(11200), "상품", 1L);

            // when
            Throwable thrown = catchThrowable(
                () -> productManageService.updateProduct(productUpdateRequest));

            // then
            assertThat(thrown)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.PRODUCT_NOT_CHANGED.getMessage());
        }

        @DisplayName("실패 - 상품이 존재하지 않음")
        @Test
        void updateProductNotFoundThrowException() {
            // given
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(155L, "상품", "K",
                "상의",
                new BigDecimal(1000), "상품", 1L);

            // when
            Throwable thrown = catchThrowable(
                () -> productManageService.updateProduct(productUpdateRequest));

            // then
            assertThat(thrown)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }

        @DisplayName("실패 - 브랜드의 카테고리에 이미 상품이 존재")
        @Test
        void updateProductDuplicatedThrowException() {
            // given
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "상품", "C",
                "상의", new BigDecimal(3000), "상품", 1L);

            // when
            Throwable thrown = catchThrowable(
                () -> productManageService.updateProduct(productUpdateRequest));

            // then
            assertThat(thrown)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.PRODUCT_DUPLICATE.getMessage());
        }

        @DisplayName("실패 - 존재하지 않는 브랜드")
        @Test
        void updateProductBrandNotFoundThrowException() {
            // given
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "상품", "r",
                "상의", new BigDecimal(3000), "상품", 1L);

            // when
            Throwable thrown = catchThrowable(
                () -> productManageService.updateProduct(productUpdateRequest));

            // then
            assertThat(thrown)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_FOUND.getMessage());
        }

        @DisplayName("실패 - 존재하지 않는 카테고리")
        @Test
        void updateProductCategoryNotFoundThrowException() {
            // given
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "상품", "A", "마바",
                new BigDecimal(3000), "상품", 1L);

            // when
            Throwable thrown = catchThrowable(
                () -> productManageService.updateProduct(productUpdateRequest));

            // then
            assertThat(thrown)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.CATEGORY_NOT_FOUND.getMessage());
        }

        @DisplayName("성공 - 상품 가격 변경")
        @Test
        void updateProductPriceOnlyChangedUpdatePrice() {
            // given
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "상품", "A", "상의",
                new BigDecimal(1500), "상품", 1L);

            // when
            productManageService.updateProduct(productUpdateRequest);

            // then
            Optional<Product> changedProduct = productRepository.findById(
                productUpdateRequest.productId());
            assertThat(changedProduct).isPresent();
            assertThat(changedProduct.get().getId()).isEqualTo(
                productUpdateRequest.productId());
            assertThat(changedProduct.get().getPrice()).isEqualTo(
                productUpdateRequest.price());
            assertThat(changedProduct.get().getBrand().getName()).isEqualTo(
                productUpdateRequest.brandName());
            assertThat(changedProduct.get().getCategory().getName()).isEqualTo(
                productUpdateRequest.categoryName());
        }

        @DisplayName("성공")
        @Test
        void updateProductSuccess() {
            // given
            ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "상품", "A",
                "상의", new BigDecimal(3000), "상품", 1L);

            // when
            productManageService.updateProduct(productUpdateRequest);

            // then
            Optional<Product> changedProduct = productRepository.findById(
                productUpdateRequest.productId());
            assertThat(changedProduct).isPresent();
            assertThat(changedProduct.get().getId()).isEqualTo(
                productUpdateRequest.productId());
            assertThat(changedProduct.get().getPrice()).isEqualTo(
                productUpdateRequest.price());
            assertThat(changedProduct.get().getBrand().getName()).isEqualTo(
                productUpdateRequest.brandName());
            assertThat(changedProduct.get().getCategory().getName()).isEqualTo(
                productUpdateRequest.categoryName());
        }
    }

    @DisplayName("상품 삭제")
    @Nested
    class ProductDelete {

        @DisplayName("성공")
        @Test
        void deleteProductSuccess() {
            // given
            Long productId = 1L;

            // when
            productManageService.deleteProduct(productId);

            // then
            assertThat(productRepository.findById(productId)).isEmpty();
        }

        @DisplayName("실패 - 존재하지 않는 상품")
        @Test
        void deleteProductNotFoundThrowsException() {
            // give
            Long productId = 99L;

            // when
            Throwable throwable = catchThrowable(
                () -> productManageService.deleteProduct(productId));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }

        @DisplayName("성공 - 상품 묶음 삭제")
        @Test
        void deleteBulkProductSuccess() {
            // give
            Long brandId = 1L;

            // when
            productManageService.deleteProductsByBrandId(brandId);

            // then
            assertThat(productRepository.findAllByBrandId(brandId)).isEmpty();
        }
    }
}
package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.config.UnitTest;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import com.devcci.devtoy.product.domain.category.Category;
import com.devcci.devtoy.product.domain.category.CategoryRepository;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.domain.product.event.ProductAdditionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductBulkDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductModificationEvent;
import com.devcci.devtoy.product.web.dto.ProductAddRequest;
import com.devcci.devtoy.product.web.dto.ProductUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@UnitTest
@Nested
class ProductManageServiceUnitTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProductManageService productManageService;

    private Product product;
    private Brand brand;
    private Category category;

    @BeforeEach
    void setUp() {
        brand = Brand.createBrand("brandA");
        category = Category.createCategory("categoryA");
        product = Product.createProduct("상품", new BigDecimal("1000"), brand, category, "상품", 1L);
    }

    @DisplayName("상품 추가")
    @Nested
    class ProductAdd {

        @DisplayName("상품 추가 성공")
        @Test
        void addProductSuccess() {
            // given
            ProductAddRequest productAddRequest = new ProductAddRequest("상품1", "brandA", "categoryA",
                new BigDecimal(1000), "상품1", 1L);
            given(brandRepository.findByName(productAddRequest.brandName())).willReturn(
                Optional.of(brand));
            given(categoryRepository.findByName(productAddRequest.categoryName())).willReturn(
                Optional.of(category));
            given(productRepository.existsByNameAndBrandNameAndCategoryName(productAddRequest.productName(),
                brand.getName(), category.getName())).willReturn(false);
            given(productRepository.save(any(Product.class))).willReturn(product);

            // when
            Product savedProduct = productManageService.addProduct(productAddRequest);

            // then
            assertThat(product).isEqualTo(savedProduct);
            assertThat(productAddRequest.brandName()).isEqualTo(
                savedProduct.getBrand().getName());
            assertThat(productAddRequest.categoryName()).isEqualTo(
                savedProduct.getCategory().getName());
            assertThat(productAddRequest.price()).isEqualTo(
                savedProduct.getPrice());
            verify(eventPublisher).publishEvent(any(ProductAdditionEvent.class));

        }

        @DisplayName("실패 - 브랜드의 카테고리에 이미 상품이 존재")
        @Test
        void addProductDuplicateThrowException() {
            // given
            ProductAddRequest productAddRequest = new ProductAddRequest("상품1", "brandA", "categoryA",
                new BigDecimal(1000), "상품1", 1L);
            given(brandRepository.findByName(productAddRequest.brandName())).willReturn(
                Optional.of(brand));
            given(categoryRepository.findByName(productAddRequest.categoryName())).willReturn(
                Optional.of(category));
            given(productRepository.existsByNameAndBrandNameAndCategoryName(productAddRequest.productName(),
                brand.getName(), category.getName())).willReturn(true);

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
            ProductAddRequest productAddRequest = new ProductAddRequest("상품", "ndB", "categoryA",
                new BigDecimal(1000), "상품", 1L);
            given(brandRepository.findByName(productAddRequest.brandName()))
                .willReturn(Optional.empty());
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
            ProductAddRequest productAddRequest = new ProductAddRequest("상품1", "brandA", "categor",
                new BigDecimal(1000), "상품1", 1L);
            given(brandRepository.findByName(productAddRequest.brandName()))
                .willReturn(Optional.ofNullable(product.getBrand()));
            given(categoryRepository.findByName(productAddRequest.categoryName()))
                .willReturn(Optional.empty());

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

        private ProductUpdateRequest productUpdateRequest;

        @DisplayName("실패 - 변경 내용 없음")
        @Test
        void updateProductNotChangedThrowException() {
            // given
            productUpdateRequest = new ProductUpdateRequest(1L, "상품", "brandA", "categoryA", new BigDecimal("1000"),
                "상품");
            Category changeCategory = Category.createCategory("categoryA");
            Brand changeBrand = Brand.createBrand("brandA");
            given(productRepository.findByIdFetchJoin(productUpdateRequest.productId()))
                .willReturn(Optional.of(product));
            given(brandRepository.findByName(productUpdateRequest.brandName()))
                .willReturn(Optional.of(changeBrand));
            given(categoryRepository.findByName(productUpdateRequest.categoryName()))
                .willReturn(Optional.of(changeCategory));

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
            productUpdateRequest = new ProductUpdateRequest(1L, "상품1", "brandA", "categoryA", new BigDecimal(1000),
                "상품1");
            given(productRepository.findByIdFetchJoin(productUpdateRequest.productId()))
                .willReturn(Optional.empty());

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
            productUpdateRequest = new ProductUpdateRequest(1L, "상품", "brandB", "categoryB", new BigDecimal(3000),
                "상품");
            Category changeCategoryName = Category.createCategory("categoryB");
            Brand changeBrandName = Brand.createBrand("brandB");
            given(productRepository.findByIdFetchJoin(productUpdateRequest.productId()))
                .willReturn(Optional.of(product));
            given(brandRepository.findByName(productUpdateRequest.brandName()))
                .willReturn(Optional.of(changeBrandName));
            given(categoryRepository.findByName(productUpdateRequest.categoryName()))
                .willReturn(Optional.of(changeCategoryName));
            given(productRepository.existsByNameAndBrandNameAndCategoryName(productUpdateRequest.productName(),
                changeBrandName.getName(), changeCategoryName.getName())).willReturn(true);

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
            productUpdateRequest = new ProductUpdateRequest(1L, "상품", "brandr", "categoryB", new BigDecimal(3000),
                "상품");
            given(productRepository.findByIdFetchJoin(productUpdateRequest.productId()))
                .willReturn(Optional.of(product));
            given(brandRepository.findByName(productUpdateRequest.brandName()))
                .willReturn(Optional.empty());

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
            productUpdateRequest = new ProductUpdateRequest(1L, "상품", "brandB", "caryB", new BigDecimal(3000), "상품");
            given(productRepository.findByIdFetchJoin(productUpdateRequest.productId()))
                .willReturn(Optional.ofNullable(product));
            given(brandRepository.findByName(productUpdateRequest.brandName()))
                .willReturn(Optional.of(brand));
            given(categoryRepository.findByName(productUpdateRequest.categoryName()))
                .willReturn(Optional.empty());

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
            productUpdateRequest = new ProductUpdateRequest(1L, "상품", "brandA", "categoryA", new BigDecimal(1500),
                "상품");
            Category changeCategory = Category.createCategory("categoryA");
            Brand changeBrand = Brand.createBrand("brandA");
            given(productRepository.findByIdFetchJoin(productUpdateRequest.productId()))
                .willReturn(Optional.of(product));
            given(brandRepository.findByName(productUpdateRequest.brandName()))
                .willReturn(Optional.of(changeBrand));
            given(categoryRepository.findByName(productUpdateRequest.categoryName()))
                .willReturn(Optional.of(changeCategory));

            // when
            productManageService.updateProduct(productUpdateRequest);

            // then
            assertThat(productUpdateRequest.brandName())
                .isEqualTo(product.getBrand().getName());
            assertThat(productUpdateRequest.categoryName())
                .isEqualTo(product.getCategory().getName());
            assertThat(productUpdateRequest.price())
                .isEqualTo(product.getPrice());
        }

        @DisplayName("성공")
        @Test
        void updateProductSuccess() {
            // given
            productUpdateRequest = new ProductUpdateRequest(1L, "상품", "brandB", "categoryB", new BigDecimal(3000),
                "상품");
            Category changeCategoryName = Category.createCategory("categoryB");
            Brand changeBrandName = Brand.createBrand("brandB");
            given(productRepository.findByIdFetchJoin(productUpdateRequest.productId()))
                .willReturn(Optional.ofNullable(product));
            given(brandRepository.findByName(productUpdateRequest.brandName()))
                .willReturn(Optional.of(changeBrandName));
            given(categoryRepository.findByName(productUpdateRequest.categoryName()))
                .willReturn(Optional.of(changeCategoryName));

            // when
            productManageService.updateProduct(productUpdateRequest);

            // then
            assertThat(productUpdateRequest.brandName())
                .isEqualTo(product.getBrand().getName());
            assertThat(productUpdateRequest.categoryName())
                .isEqualTo(product.getCategory().getName());
            assertThat(productUpdateRequest.price())
                .isEqualTo(product.getPrice());
            verify(eventPublisher).publishEvent(any(ProductModificationEvent.class));
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
            given(productRepository.findById(productId)).willReturn(
                Optional.ofNullable(product));

            // when
            productManageService.deleteProduct(productId);

            // then
            verify(productRepository).deleteById(productId);
            verify(eventPublisher).publishEvent(any(ProductDeletionEvent.class));
        }

        @DisplayName("실패 - 존재하지 않는 상품")
        @Test
        void deleteProductNotFoundThrowsException() {
            // give
            Long productId = 1L;
            given(productRepository.findById(productId)).willReturn(Optional.empty());

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
            Brand brand1 = Brand.createBrand("brandB");
            Category category1 = Category.createCategory("categoryB");
            Brand brand2 = Brand.createBrand("brandB");
            Category category2 = Category.createCategory("categoryB");
            Product product1 =
                Product.createProduct("상품1", new BigDecimal(1000), brand1, category1, "상품1", 1L);
            Product product2 =
                Product.createProduct("상품2", new BigDecimal(2000), brand2, category2, "상품2", 1L);
            List<Product> products = List.of(product1, product2);
            given(productRepository.findAllByBrandId(brandId)).willReturn(products);

            // when
            productManageService.deleteProductsByBrandId(brandId);

            // then
            verify(productRepository).deleteAllInBatch(products);
            verify(eventPublisher).publishEvent(any(ProductBulkDeletionEvent.class));
        }
    }

}

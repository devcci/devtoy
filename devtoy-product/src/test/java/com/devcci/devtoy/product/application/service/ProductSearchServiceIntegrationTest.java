package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse.LowestPriceBrandProduct.BrandProduct;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse.CategoryProduct;
import com.devcci.devtoy.product.application.dto.ProductInfo;
import com.devcci.devtoy.product.application.dto.ProductInfos;
import com.devcci.devtoy.product.config.IntegrationTest;
import com.devcci.devtoy.product.infra.redis.ProductViewRedisService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@IntegrationTest
class ProductSearchServiceIntegrationTest {

    @Autowired
    private ProductSearchService productSearchService;

    @Autowired
    private ProductViewRedisService productViewRedisService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @AfterEach
    public void clearRedis() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushDb();
    }

    @DisplayName("상품 조회")
    @Nested
    class ProductSearch {

        @DisplayName("성공 - 상품 단건 조회")
        @Test
        void findProductById() {
            // given
            Long productId = 1L;

            // when
            ProductInfo productInfo = productSearchService.findProductById(productId);

            // then
            assertThat(productInfo).isNotNull();
            assertThat(productInfo.getCategoryName()).isEqualTo("상의");
            assertThat(productInfo.getBrandName()).isEqualTo("A");
            assertThat(productInfo.getPrice()).isEqualTo("11,200");
            Double viewCount = productViewRedisService.getProductViewCount(productId);
            assertThat(viewCount).isEqualTo(1);
        }

        @DisplayName("성공 - 상품 단건 조회 - 레디스 스레드세이프")
        @Test
        void findProductByIdWithMultiThread() throws InterruptedException {
            // given
            int threadCount = 50;
            ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
            CountDownLatch readyLatch = new CountDownLatch(threadCount);
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(threadCount);

            Long productId = 1L;

            // when
            Runnable task = () -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    productSearchService.findProductById(productId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            };

            for (int i = 0; i < threadCount; i++) {
                executorService.execute(task);
            }

            readyLatch.await();
            startLatch.countDown();
            doneLatch.await();
            executorService.shutdown();

            // then
            Double viewCount = productViewRedisService.getProductViewCount(productId);
            assertThat(viewCount).isGreaterThanOrEqualTo(50);
        }

        @DisplayName("성공 - 상품 목록 조회")
        @Test
        void findAllProductSuccess() {
            // given
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "id"));

            // when
            ProductInfos productInfos = productSearchService.findAllProduct(pageable);

            // then
            assertThat(productInfos.getProductInfos()).hasSize(10);
            ProductInfo product = productInfos.getProductInfos().get(0);
            assertThat(product.getCategoryName()).isEqualTo("상의");
            assertThat(product.getBrandName()).isEqualTo("A");
            assertThat(product.getPrice()).isEqualTo("11,200");
        }
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
        assertThat(brandProducts.get(0).getCategoryName()).isEqualTo("모자");
        assertThat(brandProducts.get(0).getPrice()).isEqualTo("1,500");
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
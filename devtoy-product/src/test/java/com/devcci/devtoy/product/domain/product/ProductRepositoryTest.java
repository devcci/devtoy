package com.devcci.devtoy.product.domain.product;

import com.devcci.devtoy.product.config.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RepositoryTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("특정 브랜드에 속하는 모든 상품 조회")
    @Test
    void findAllByBrandId() {
        // given
        Long brandId = 1L;

        // when
        List<Product> productsByBrand = productRepository.findAllByBrandId(brandId);

        // then
        assertThat(productsByBrand).hasSize(8);
    }
}
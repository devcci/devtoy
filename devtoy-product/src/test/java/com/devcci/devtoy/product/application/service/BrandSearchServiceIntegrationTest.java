package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.product.application.dto.BrandResponse;
import com.devcci.devtoy.product.config.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Nested
@IntegrationTest
class BrandSearchServiceIntegrationTest {

    @Autowired
    private BrandSearchService brandSearchService;

    @DisplayName("성공 - 전체 브랜드 조회")
    @Test
    void findAllBrand() {
        // when
        List<BrandResponse> brandResponses = brandSearchService.findAllBrand();

        // then
        assertThat(brandResponses).hasSize(9);
        assertThat(brandResponses.get(0).getId()).isEqualTo(1L);
        assertThat(brandResponses.get(0).getBrandName()).isEqualTo("A");
        assertThat(brandResponses.get(1).getId()).isEqualTo(2L);
        assertThat(brandResponses.get(1).getBrandName()).isEqualTo("B");
    }
}

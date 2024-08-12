package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.product.application.dto.BrandResponse;
import com.devcci.devtoy.product.common.exception.ApiException;
import com.devcci.devtoy.product.common.exception.ErrorCode;
import com.devcci.devtoy.product.config.UnitTest;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@UnitTest
class BrandSearchServiceUnitTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandSearchService brandSearchService;

    List<Brand> brands;

    @BeforeEach
    void setUp() {
        Brand brand1 = Brand.createBrand("testBrand1");
        Brand brand2 = Brand.createBrand("testBrand2");
        brands = List.of(brand1, brand2);
    }


    @DisplayName("성공 - 전체 브랜드 조회")
    @Test
    void findAllBrand() {
        // given
        given(brandRepository.findAll()).willReturn(brands);

        // when
        List<BrandResponse> brandResponses = brandSearchService.findAllBrand();

        // then
        assertThat(brandResponses).hasSize(2);
        assertThat(brandResponses.get(0).getName()).isEqualTo(brands.get(0).getName());
        assertThat(brandResponses.get(1).getName()).isEqualTo(brands.get(1).getName());
        verify(brandRepository, times(1)).findAll();
    }

    @DisplayName("실패 - 브랜드 목록이 존재하지 않음")
    @Test
    void findAllBrandNotFoundException() {
        // given
        given(brandRepository.findAll()).willReturn(Collections.emptyList());

        // when
        Throwable throwable = catchThrowable(() -> brandSearchService.findAllBrand());

        // then
        verify(brandRepository, times(1)).findAll();
        assertThat(throwable)
            .isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.BRAND_LIST_NOT_LOADED.getMessage());
    }

}
package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.config.IntegrationTest;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import com.devcci.devtoy.product.web.dto.UpdateBrandRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@IntegrationTest
class BrandManageServiceIntegrationTest {

    @Autowired
    BrandRepository brandRepository;
    @Autowired
    BrandManageService brandManageService;

    @DisplayName("브랜드 추가")
    @Nested
    class AddBrandTest {

        @DisplayName("성공")
        @Test
        void addBrandSuccess() {
            // given
            String brandName = "testBrand";

            // when
            Brand result = brandManageService.addBrand(brandName);

            // then
            Brand savedBrand = brandRepository.findByName(brandName).orElseThrow();
            assertThat(savedBrand.getId()).isEqualTo(result.getId());
            assertThat(savedBrand.getName()).isEqualTo(brandName);
        }


        @DisplayName("실패 - 브랜드 이름 중복")
        @Test
        void addBrandDuplicateThrowsException() {
            // given
            String brandName = "A";

            // when
            Throwable throwable = catchThrowable(() -> brandManageService.addBrand(brandName));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_DUPLICATE.getMessage());
        }
    }

    @DisplayName("브랜드 수정")
    @Nested
    class UpdateBrandTest {

        @DisplayName("성공")
        @Test
        void updateBrandSuccess() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(1L, "newBrand");

            // when
            brandManageService.updateBrand(request.brandId(), request.brandName());

            // then
            Optional<Brand> brand = brandRepository.findById(request.brandId());
            assertThat(brand).isPresent();
            assertThat(brand.get().getName()).isEqualTo(request.brandName());
        }

        @DisplayName("실패 - 존재하지 않는 브랜드")
        @Test
        void updateBrandNotFoundThrowsException() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(99L, "newBrand");

            // when
            Throwable throwable = catchThrowable(() ->
                brandManageService.updateBrand(request.brandId(), request.brandName()));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_FOUND.getMessage());
        }

        @DisplayName("실패 - 변경 내용 없음")
        @Test
        void updateBrandNotChangeThrowsException() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(1L, "A");

            // when
            Throwable throwable = catchThrowable(
                () -> brandManageService.updateBrand(request.brandId(), request.brandName()));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_CHANGED.getMessage());
        }

        @DisplayName("실패 - 중복 브랜드")
        @Test
        void updateBrandDuplicateThrowsException() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(1L, "B");

            // when
            Throwable throwable = catchThrowable(
                () -> brandManageService.updateBrand(request.brandId(), request.brandName()));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_DUPLICATE.getMessage());
        }
    }

    @DisplayName("브랜드 삭제")
    @Nested
    class DeleteBrandTest {

        @DisplayName("성공")
        @Test
        void deleteBrandSuccess() {
            // given
            Long brandId = 1L;

            // when
            brandManageService.deleteBrand(brandId);

            // then
            assertThat(brandRepository.findById(brandId)).isEmpty();
        }

        @DisplayName("실패 - 존재하지 않는 브랜드")
        @Test
        void deleteBrandNotFoundThrowsException() {

            // given
            Long brandId = 99L;

            // when
            Throwable throwable = catchThrowable(() -> brandManageService.deleteBrand(brandId));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_FOUND.getMessage());
        }
    }

}
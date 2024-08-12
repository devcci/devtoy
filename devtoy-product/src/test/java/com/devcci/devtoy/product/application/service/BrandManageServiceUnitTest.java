package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.product.common.exception.ApiException;
import com.devcci.devtoy.product.common.exception.ErrorCode;
import com.devcci.devtoy.product.config.UnitTest;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import com.devcci.devtoy.product.domain.brand.event.BrandDeletionEvent;
import com.devcci.devtoy.product.domain.brand.event.BrandModificationEvent;
import com.devcci.devtoy.product.web.dto.CreateBrandRequest;
import com.devcci.devtoy.product.web.dto.UpdateBrandRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@Nested
@UnitTest
class BrandManageServiceUnitTest {

    @InjectMocks
    BrandManageService brandManageService;

    @Mock
    BrandRepository brandRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    Brand brand;

    @BeforeEach
    void setUp() {
        brand = Brand.createBrand("brand");
    }

    @DisplayName("브랜드 추가")
    @Nested
    class BrandAddTest {

        @DisplayName("성공")
        @Test
        void addBrandSuccess() {
            // given
            String brandName = "brand";
            given(brandRepository.existsByName(brandName)).willReturn(false);
            given(brandRepository.save(any(Brand.class))).willReturn(brand);

            // when
            Brand result = brandManageService.addBrand(brandName);

            //then
            assertThat(brand).isEqualTo(result);
            assertThat(brand.getId()).isEqualTo(result.getId());
            assertThat(brand.getName()).isEqualTo(result.getName());
        }

        @DisplayName("실패 - 브랜드 이름 중복")
        @Test
        void addBrandDuplicateThrowsException() {
            // given
            CreateBrandRequest request = new CreateBrandRequest("newBrand");
            given(brandRepository.existsByName(request.brandName())).willReturn(true);

            // when
            Throwable throwable = catchThrowable(
                () -> brandManageService.addBrand(request.brandName()));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_DUPLICATE.getMessage());
        }
    }

    @DisplayName("브랜드 수정")
    @Nested
    class BrandUpdateTest {

        @DisplayName("성공")
        @Test
        void updateBrandSuccess() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(1L, "updateBrand");
            given(brandRepository.findById(request.brandId())).willReturn(
                Optional.ofNullable(brand));
            given(brandRepository.existsByName(request.brandName())).willReturn(false);

            // when
            brandManageService.updateBrand(request.brandId(), request.brandName());

            // then
            assertThat(brand.getName()).isEqualTo(request.brandName());
            verify(eventPublisher).publishEvent(any(BrandModificationEvent.class));
        }

        @DisplayName("실패 - 존재하지 않는 브랜드")
        @Test
        void updateBrandNotFoundThrowsException() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(1L, "updateBrand");
            given(brandRepository.findById(request.brandId())).willReturn(Optional.empty());

            // when
            Throwable throwable = catchThrowable(
                () -> brandManageService.updateBrand(request.brandId(), request.brandName()));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_FOUND.getMessage());
        }

        @DisplayName("실패 - 변경 내용 없음")
        @Test
        void updateBrandNotChangeThrowsException() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(1L, "brand");
            given(brandRepository.findById(request.brandId())).willReturn(
                Optional.ofNullable(brand));

            // when
            Throwable throwable = catchThrowable(
                () -> brandManageService.updateBrand(request.brandId(), request.brandName()));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_CHANGED.getMessage());
        }

        @DisplayName("실패 - 중복된 브랜드")
        @Test
        void updateBrandDuplicateThrowsException() {
            // given
            UpdateBrandRequest request = new UpdateBrandRequest(1L, "newBrand");
            given(brandRepository.findById(request.brandId())).willReturn(
                Optional.ofNullable(brand));
            given(brandRepository.existsByName(request.brandName())).willReturn(true);

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
    class BrandDeleteTest {

        @DisplayName("성공")
        @Test
        void deleteBrandSuccess() {
            // give
            Long brandId = 1L;
            given(brandRepository.findById(brandId)).willReturn(Optional.ofNullable(brand));

            // when
            brandManageService.deleteBrand(brandId);

            // then
            verify(brandRepository).delete(brand);
            verify(eventPublisher).publishEvent(any(BrandDeletionEvent.class));
        }

        @DisplayName("실패 - 존재하지 않는 브랜드")
        @Test
        void deleteBrandNotFoundThrowsException() {
            // give
            Long brandId = 1L;
            given(brandRepository.findById(brandId)).willReturn(Optional.empty());

            // when
            Throwable throwable = catchThrowable(() -> brandManageService.deleteBrand(brandId));

            // then
            assertThat(throwable)
                .isInstanceOf(ApiException.class)
                .hasMessageContaining(ErrorCode.BRAND_NOT_FOUND.getMessage());
        }
    }

}

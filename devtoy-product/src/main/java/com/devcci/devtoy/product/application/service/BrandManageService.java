package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.product.common.exception.ApiException;
import com.devcci.devtoy.product.common.exception.ErrorCode;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import com.devcci.devtoy.product.domain.brand.event.BrandAdditionEvent;
import com.devcci.devtoy.product.domain.brand.event.BrandDeletionEvent;
import com.devcci.devtoy.product.domain.brand.event.BrandModificationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandManageService {

    private final BrandRepository brandRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BrandManageService(BrandRepository brandRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.brandRepository = brandRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Brand addBrand(String brandName) {
        if (brandRepository.existsByName(brandName)) {
            throw new ApiException(ErrorCode.BRAND_DUPLICATE);
        }
        Brand newBrand = Brand.createBrand(brandName);
        Brand brand = brandRepository.save(newBrand);
        eventPublisher.publishEvent(new BrandAdditionEvent(brand.getId()));
        return brand;
    }

    @Transactional
    public void updateBrand(Long brandId, String newName) {
        Brand brand = brandRepository.findById(brandId)
            .orElseThrow(() -> new ApiException(ErrorCode.BRAND_NOT_FOUND));
        if (brand.getName().equals(newName)) {
            throw new ApiException(ErrorCode.BRAND_NOT_CHANGED);
        } else if (brandRepository.existsByName(newName)) {
            throw new ApiException(ErrorCode.BRAND_DUPLICATE);
        } else {
            brand.updateBrandName(newName);
            eventPublisher.publishEvent(new BrandModificationEvent(brand.getId()));
        }
    }

    @Transactional
    public void deleteBrand(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
            .orElseThrow(() -> new ApiException(ErrorCode.BRAND_NOT_FOUND));
        eventPublisher.publishEvent(new BrandDeletionEvent(brand.getId()));
        brandRepository.delete(brand);
    }

}

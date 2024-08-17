package com.devcci.devtoy.product.application.service;


import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.application.dto.BrandResponse;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandSearchService {

    private final BrandRepository brandRepository;

    public BrandSearchService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Transactional(readOnly = true)
    public List<BrandResponse> findAllBrand() {
        List<Brand> brands = brandRepository.findAll();
        if (brands.isEmpty()) {
            throw new ApiException(ErrorCode.BRAND_LIST_NOT_LOADED);
        }
        return brands.stream()
            .map(BrandResponse::of)
            .toList();
    }

}

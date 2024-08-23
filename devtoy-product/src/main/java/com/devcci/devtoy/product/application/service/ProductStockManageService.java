package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductStockManageService {
    private final ProductRepository productRepository;

    public ProductStockManageService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void modifyStockQuantity(Long productId, Long quantity) {
        productRepository.findById(productId)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND))
            .modifyStockQuantity(quantity);
    }

    @Transactional
    public void removeStockQuantity(Long productId, Long quantity) {
        productRepository.findById(productId)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND))
            .removeStockQuantity(quantity);
    }
}

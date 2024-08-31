package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.domain.product.event.ProductModificationEvent;
import com.devcci.devtoy.product.domain.product.event.ProductOrderEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductStockManageService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductStockManageService(ProductRepository productRepository,
        ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void modifyStockQuantity(Long productId, Long quantity) {
        productRepository.findById(productId)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND))
            .modifyStockQuantity(quantity);
        eventPublisher.publishEvent(new ProductModificationEvent(productId));
    }

    @Transactional
    public void removeStockQuantity(OrderMessage orderMessage) {
        orderMessage.orderProducts().forEach(
            orderProductMessage -> {
                Product product = productRepository.findByIdWithPessimisticLock(orderProductMessage.productId())
                    .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                product.validatePrice(orderProductMessage.price());
                product.removeStockQuantity(orderProductMessage.quantity());
            }
        );
        eventPublisher.publishEvent(new ProductOrderEvent(orderMessage));
    }
}

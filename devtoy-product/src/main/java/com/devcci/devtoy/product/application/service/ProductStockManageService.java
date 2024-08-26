package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductStockManageService {
    private final ProductRepository productRepository;
    private final OrderResultMessageProducer orderResultMessageProducer;

    public ProductStockManageService(ProductRepository productRepository, OrderResultMessageProducer orderResultMessageProducer) {
        this.productRepository = productRepository;
        this.orderResultMessageProducer = orderResultMessageProducer;
    }

    @Transactional
    public void modifyStockQuantity(Long productId, Long quantity) {
        productRepository.findById(productId)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND))
            .modifyStockQuantity(quantity);
    }

    @Transactional
    public void removeStockQuantity(OrderMessage orderMessage) {
        orderMessage.orderProducts().forEach(
            orderProductMessage -> {
                Product product = productRepository.findById(orderProductMessage.productId())
                    .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
                product.validatePrice(orderProductMessage.price());
                product.removeStockQuantity(orderProductMessage.quantity());
            }
        );
        orderResultMessageProducer.send(orderMessage.orderId().toString(), new OrderResultMessage(orderMessage.orderId().toString(), "COMPLETED", null));
    }
}

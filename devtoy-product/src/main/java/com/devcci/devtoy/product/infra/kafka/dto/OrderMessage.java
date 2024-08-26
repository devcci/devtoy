package com.devcci.devtoy.product.infra.kafka.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderMessage(Long orderId, String memberId, List<OrderProductMessage> orderProducts) {
    public record OrderProductMessage(Long productId, Long quantity, BigDecimal price) {
    }
}

package com.devcci.devtoy.common.infra.kafka.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderMessage(Long orderId, String memberId, List<OrderProductMessage> orderProducts) {
    public static OrderMessage of(Long orderId, String memberId, List<OrderProductMessage> orderProducts) {
        return new OrderMessage(orderId, memberId, orderProducts);
    }

    public record OrderProductMessage(Long productId, Long quantity, BigDecimal price) {
        public static OrderProductMessage of(Long productId, Long quantity, BigDecimal price) {
            return new OrderProductMessage(productId, quantity, price);
        }
    }
}

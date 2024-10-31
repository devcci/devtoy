package com.devcci.devtoy.common.infra.kafka.dto;

import com.devcci.devtoy.common.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderEventMessage(
    Long orderId,
    String memberId,
    List<OrderProductMessage> orderProducts,
    OrderStatus status
) {

    public static OrderEventMessage of(Long orderId, String memberId, List<OrderProductMessage> orderProducts,
        OrderStatus status) {
        return new OrderEventMessage(orderId, memberId, orderProducts, status);
    }

    public record OrderProductMessage(Long productId, Long quantity, BigDecimal price) {

        public static OrderProductMessage of(Long productId, Long quantity, BigDecimal price) {
            return new OrderProductMessage(productId, quantity, price);
        }
    }
}

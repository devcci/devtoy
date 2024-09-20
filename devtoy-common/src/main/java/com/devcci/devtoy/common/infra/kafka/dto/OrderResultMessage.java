package com.devcci.devtoy.common.infra.kafka.dto;

import com.devcci.devtoy.common.domain.OrderStatus;

public record OrderResultMessage(String orderId, OrderStatus status, String reason) {

    public static OrderResultMessage of(String orderId, OrderStatus status, String reason) {
        return new OrderResultMessage(orderId, status, reason);
    }
}

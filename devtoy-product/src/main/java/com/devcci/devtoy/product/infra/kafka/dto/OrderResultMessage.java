package com.devcci.devtoy.product.infra.kafka.dto;

public record OrderResultMessage(String orderId, String status, String reason) {
    public static OrderResultMessage of(String orderId, String status, String reason) {
        return new OrderResultMessage(orderId, status, reason);
    }
}

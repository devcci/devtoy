package com.devcci.devtoy.order.infra.kafka.dto;

public record OrderResultMessage(String orderId, String status, String reason) {
}

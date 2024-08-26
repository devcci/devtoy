package com.devcci.devtoy.order.application.dto;

public record OrderResponse(Long orderId, String memberId, String orderStatus, String statusReason) {
    public static OrderResponse of(Long orderId, String memberId, String orderStatus, String statusReason) {
        return new OrderResponse(orderId, memberId, orderStatus, statusReason);
    }
}

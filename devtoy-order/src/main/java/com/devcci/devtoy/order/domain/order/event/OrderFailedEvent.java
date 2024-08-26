package com.devcci.devtoy.order.domain.order.event;

public record OrderFailedEvent(Long orderId, String reason) {
}

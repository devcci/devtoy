package com.devcci.devtoy.order.domain.order.event;

import com.devcci.devtoy.order.domain.order.Order;

public record OrderCreatedEvent(Order order) {
}

package com.devcci.devtoy.order.domain.order.event;

import com.devcci.devtoy.order.web.dto.OrderRequest;

public record OrderCreatedEvent(Long memberId, OrderRequest orderRequests) {
}

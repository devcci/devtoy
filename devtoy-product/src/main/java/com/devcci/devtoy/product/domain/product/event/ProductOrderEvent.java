package com.devcci.devtoy.product.domain.product.event;

import com.devcci.devtoy.common.infra.kafka.dto.OrderEventMessage;

public record ProductOrderEvent(OrderEventMessage orderEventMessage) {

}

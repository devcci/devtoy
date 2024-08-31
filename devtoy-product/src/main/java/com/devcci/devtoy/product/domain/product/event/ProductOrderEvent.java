package com.devcci.devtoy.product.domain.product.event;

import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;

public record ProductOrderEvent(OrderMessage orderMessage) {

}

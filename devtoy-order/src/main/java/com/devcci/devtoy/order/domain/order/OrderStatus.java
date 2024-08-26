package com.devcci.devtoy.order.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED,
    CANCELED,
    COMPLETED;
}

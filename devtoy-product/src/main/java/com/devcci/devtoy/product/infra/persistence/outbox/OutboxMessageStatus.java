package com.devcci.devtoy.product.infra.persistence.outbox;

import lombok.Getter;

@Getter
public enum OutboxMessageStatus {
    PENDING, SENT
}

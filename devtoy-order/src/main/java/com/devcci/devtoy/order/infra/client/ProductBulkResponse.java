package com.devcci.devtoy.order.infra.client;

import java.math.BigDecimal;

public record ProductBulkResponse(Long productId, BigDecimal price, Long stockQuantity) {
}

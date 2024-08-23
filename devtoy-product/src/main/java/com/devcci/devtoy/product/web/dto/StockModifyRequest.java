package com.devcci.devtoy.product.web.dto;

import jakarta.validation.constraints.NotNull;

public record StockModifyRequest(@NotNull Long quantity) {
}

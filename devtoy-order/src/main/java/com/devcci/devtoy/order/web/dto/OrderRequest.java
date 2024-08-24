package com.devcci.devtoy.order.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public record OrderRequest(@NotNull @Valid List<OrderProductRequest> orderProductRequests) {
    public record OrderProductRequest(
        @NotNull Long productId,
        @NotNull @Min(1) Long quantity) {
    }
}

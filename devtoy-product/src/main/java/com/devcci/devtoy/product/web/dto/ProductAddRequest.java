package com.devcci.devtoy.product.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductAddRequest(
    @NotBlank @Size(min = 2, max = 20) String productName,
    @NotBlank @Size(max = 20, message = "20글자 이하로 작성해주세요.") String brandName,
    @NotBlank @Size(max = 20, message = "20글자 이하로 작성해주세요.") String categoryName,
    BigDecimal price,
    String description,
    @NotNull Long stockQuantity
) {

}

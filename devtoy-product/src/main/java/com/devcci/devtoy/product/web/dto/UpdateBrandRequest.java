package com.devcci.devtoy.product.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBrandRequest(
    @NotNull Long brandId,
    @NotBlank @Size(max = 20, message = "20글자 이하로 작성해주세요.") String brandName) {

}

package com.devcci.devtoy.product.application.dto;

import com.devcci.devtoy.product.domain.brand.Brand;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BrandResponse {

    private Long id;
    private String name;

    private BrandResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static BrandResponse of(Brand brand) {
        return new BrandResponse(
            brand.getId(),
            brand.getName()
        );
    }
}

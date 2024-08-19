package com.devcci.devtoy.product.web.api;


import com.devcci.devtoy.product.application.dto.BrandResponse;
import com.devcci.devtoy.product.application.service.BrandSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "브랜드 조회 API")
@RequestMapping("/brand")
@RestController
public class BrandSearchController {

    private final BrandSearchService brandSearchService;

    public BrandSearchController(BrandSearchService brandSearchService) {
        this.brandSearchService = brandSearchService;
    }

    @Operation(summary = "브랜드 목록 조회")
    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrand() {
        List<BrandResponse> allBrands = brandSearchService.findAllBrand();
        return ResponseEntity.ok().body(allBrands);
    }
}

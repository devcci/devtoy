package com.devcci.devtoy.product.web.api;


import com.devcci.devtoy.product.application.service.BrandManageService;
import com.devcci.devtoy.product.web.dto.CreateBrandRequest;
import com.devcci.devtoy.product.web.dto.UpdateBrandRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Tag(name = "브랜드 관리 API")
@RequestMapping("/brand")
@RestController
public class BrandManageController {

    private final BrandManageService brandManageService;
    private final String gatewayUri;

    public BrandManageController(BrandManageService brandManageService, @Value("${gateway.uri}") String gatewayUri) {
        this.brandManageService = brandManageService;
        this.gatewayUri = gatewayUri;
    }

    @Operation(summary = "브랜드 추가")
    @PostMapping
    public ResponseEntity<Void> addBrand(@RequestBody @Valid CreateBrandRequest dto) {
        Long newBrandId = brandManageService.addBrand(dto.brandName()).getId();
        URI location = UriComponentsBuilder
            .fromUriString(gatewayUri)
            .path("/brand/{id}")
            .buildAndExpand(newBrandId)
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "브랜드 수정")
    @PutMapping
    public ResponseEntity<Void> updateBrand(
        @RequestBody @Valid UpdateBrandRequest dto
    ) {
        brandManageService.updateBrand(dto.brandId(), dto.brandName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "브랜드 삭제")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<Void> deleteBrand(@PathVariable Long brandId) {
        brandManageService.deleteBrand(brandId);
        return ResponseEntity.noContent().build();
    }
}

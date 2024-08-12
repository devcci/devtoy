package com.devcci.devtoy.product.web.api;


import com.devcci.devtoy.product.application.service.BrandManageService;
import com.devcci.devtoy.product.web.dto.CreateBrandRequest;
import com.devcci.devtoy.product.web.dto.UpdateBrandRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "브랜드 관리 API")
@RequestMapping("/brands")
@RestController
public class BrandManageController {

    private final BrandManageService brandManageService;

    public BrandManageController(BrandManageService brandManageService) {
        this.brandManageService = brandManageService;
    }

    @Operation(summary = "브랜드 추가")
    @PostMapping
    public ResponseEntity<Void> addBrand(@RequestBody @Valid CreateBrandRequest dto) {
        Long newBrandId = brandManageService.addBrand(dto.brandName()).getId();
        return ResponseEntity.created(URI.create("/brands/" + newBrandId)).build();
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

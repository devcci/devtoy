package com.devcci.devtoy.product.web.api;


import com.devcci.devtoy.product.application.service.ProductManageService;
import com.devcci.devtoy.product.web.dto.ProductAddRequest;
import com.devcci.devtoy.product.web.dto.ProductUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "상품 관리 API")
@RequestMapping("/products")
@RestController
public class ProductManageController {

    private final ProductManageService productManageService;

    @Autowired
    public ProductManageController(ProductManageService productManageService) {
        this.productManageService = productManageService;
    }

    @Operation(summary = "상품 추가")
    @PostMapping
    public ResponseEntity<Void> addProduct(
        @RequestBody @Valid ProductAddRequest productAddRequest) {
        long newProductId = productManageService.addProduct(productAddRequest).getId();
        return ResponseEntity.created(URI.create("products/" + newProductId)).build();
    }

    @Operation(summary = "상품 수정")
    @PutMapping
    public ResponseEntity<Void> updateProduct(
        @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        productManageService.updateProduct(productUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productManageService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}

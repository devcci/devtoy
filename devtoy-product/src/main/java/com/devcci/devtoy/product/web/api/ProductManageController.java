package com.devcci.devtoy.product.web.api;


import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage.OrderProductMessage;
import com.devcci.devtoy.product.application.service.ProductManageService;
import com.devcci.devtoy.product.application.service.ProductStockManageService;
import com.devcci.devtoy.product.web.dto.ProductAddRequest;
import com.devcci.devtoy.product.web.dto.ProductUpdateRequest;
import com.devcci.devtoy.product.web.dto.StockModifyRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Tag(name = "상품 관리 API")
@RequestMapping("/product")
@RestController
public class ProductManageController {

    private final ProductManageService productManageService;
    private final ProductStockManageService productStockManageService;
    private final String gatewayUri;

    @Autowired
    public ProductManageController(ProductManageService productManageService,
        ProductStockManageService productStockManageService,
        @Value("${gateway.uri}") String gatewayUri) {
        this.productManageService = productManageService;
        this.productStockManageService = productStockManageService;
        this.gatewayUri = gatewayUri;
    }

    @Operation(summary = "상품 추가")
    @PostMapping
    public ResponseEntity<Void> addProduct(
        @RequestBody @Valid ProductAddRequest productAddRequest) {
        long newProductId = productManageService.addProduct(productAddRequest).getId();
        URI location = UriComponentsBuilder
            .fromUriString(gatewayUri)
            .path("/product/{id}")
            .buildAndExpand(newProductId)
            .toUri();
        return ResponseEntity.created(location).build();
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

    @Operation(summary = "상품 재고 수정")
    @PutMapping("/stock/{productId}")
    public ResponseEntity<Void> modifyStockQuantity(@PathVariable("productId") Long productId,
        @Valid @RequestBody StockModifyRequest stockModifyRequest) {
        productStockManageService.modifyStockQuantity(productId, stockModifyRequest.quantity());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public void test() {
        OrderProductMessage orderProductMessage = OrderProductMessage.of(1L, 1L, new BigDecimal("11200"));

        productStockManageService.removeStockQuantity(OrderMessage.of(1L, "test", List.of(orderProductMessage)));
    }
}

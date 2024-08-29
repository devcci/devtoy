package com.devcci.devtoy.product.web.api;

import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import com.devcci.devtoy.product.application.dto.ProductBulkResponse;
import com.devcci.devtoy.product.application.dto.ProductResponse;
import com.devcci.devtoy.product.application.service.ProductSearchService;
import com.devcci.devtoy.product.common.util.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Tag(name = "상품 조회 API")
@Validated
@RequestMapping("/product")
@RestController
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    public ProductSearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @Operation(summary = "상품 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
        @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productSearchService.findProductById(productId));
    }

    @Operation(summary = "상품 Bulk 조회")
    @GetMapping("/bulk")
    public ResponseEntity<List<ProductBulkResponse>> getProductsByIds(
        @RequestParam("productIds") Set<Long> productIds) {
        return ResponseEntity.ok(productSearchService.findProductsByIds(productIds));
    }

    @Operation(summary = "상품 목록 조회")
    @Parameters(value = {
        @Parameter(name = "page", description = "결과 페이지 번호"),
        @Parameter(name = "size", description = "한 페이지에 보여질 문서 수"),
        @Parameter(name = "sort", description = "결과 문서 정렬 방식 ASC | DESC")}
    )
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(
        @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
        @RequestParam(name = "page", required = false, defaultValue = "1")
        Integer page,
        @Min(value = 1, message = "사이즈는 1 이상이어야 합니다.")
        @RequestParam(name = "size", required = false, defaultValue = "10")
        Integer size,
        @RequestParam(value = "sort", required = false) String sort) {
        List<ProductResponse> allProduct = productSearchService.findAllProduct(
            PageableUtil.createPageable(page, size, sort));
        return ResponseEntity.ok().body(allProduct);
    }

    @Operation(summary = "카테고리별 최저가 브랜드 상품 조회")
    @GetMapping("/categories/lowest")
    public ResponseEntity<LowestPriceCategoryResponse> getLowestPriceCategory() {
        return ResponseEntity.ok(productSearchService.getLowestPriceProductPerCategory());
    }

    @Operation(summary = "최저가 브랜드의 모든 카테고리 상품 조회")
    @GetMapping("/brands/lowest")
    public ResponseEntity<LowestPriceBrandProductsResponse> getLowestPriceBrand() {
        return ResponseEntity.ok(productSearchService.getLowestPriceProductByBrand());
    }

    @Operation(summary = "카테고리의 최저가, 최고가 브랜드 상품 조회")
    @GetMapping("/categories/min-max-prices")
    public ResponseEntity<CategoryPriceRangeResponse> getCategoryMinMaxPrices(
        @NotBlank(message = "카테고리명을 입력해주세요.")
        @RequestParam(value = "categoryName") String categoryName
    ) {
        return ResponseEntity.ok(productSearchService.getCategoryMinMaxPrices(categoryName));
    }
}

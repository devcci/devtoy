package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse;
import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse.HighestPriceProduct;
import com.devcci.devtoy.product.application.dto.CategoryPriceRangeResponse.LowestPriceProduct;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse.LowestPriceBrandProduct;
import com.devcci.devtoy.product.application.dto.LowestPriceBrandProductsResponse.LowestPriceBrandProduct.BrandProduct;
import com.devcci.devtoy.product.application.dto.LowestPriceCategoryResponse;
import com.devcci.devtoy.product.application.dto.ProductBulkResponse;
import com.devcci.devtoy.product.application.dto.ProductResponse;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.domain.product.event.ProductViewEvent;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByBrandProjection;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByCategoryProjection;
import com.devcci.devtoy.product.infra.persistence.projection.PriceByCategoryProjection;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductSearchService(ProductRepository productRepository,
        ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public ProductResponse findProductById(Long productId) {
        Product product = productRepository.findByIdFetchJoin(productId)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        eventPublisher.publishEvent(new ProductViewEvent(productId));
        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public List<ProductBulkResponse> findProductsByIds(Set<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products.isEmpty()) {
            throw new ApiException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return products.stream()
            .map(ProductBulkResponse::of)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllProduct(Pageable pageable) {
        List<Product> products = productRepository.findAllFetchJoin(pageable);
        if (products.isEmpty()) {
            throw new ApiException(ErrorCode.PRODUCT_LIST_NOT_LOADED);
        }
        return products.stream()
            .map(ProductResponse::of)
            .toList();
    }

    @Cacheable(value = "lowestPriceCategory")
    @Transactional(readOnly = true)
    public LowestPriceCategoryResponse getLowestPriceProductPerCategory() {
        List<LowestProductByCategoryProjection> productsPerCategory = productRepository.findLowestPriceProductByCategory();
        if (productsPerCategory.isEmpty()) {
            throw new ApiException(ErrorCode.CATEGORY_LOWEST_PRICE_PRODUCT_ERROR);
        }

        List<LowestPriceCategoryResponse.CategoryProduct> categoryProducts = productsPerCategory.stream()
            .map(p -> LowestPriceCategoryResponse.CategoryProduct.createCategoryProduct(
                p.getProductName(),
                p.getCategoryName(),
                p.getBrandName(),
                p.getProductPrice()))
            .toList();

        BigDecimal totalPrice = productsPerCategory.stream()
            .map(LowestProductByCategoryProjection::getProductPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return LowestPriceCategoryResponse.createLowestPriceBrandCategory(categoryProducts,
            totalPrice);
    }

    @Cacheable(value = "lowestPriceBrand")
    @Transactional(readOnly = true)
    public LowestPriceBrandProductsResponse getLowestPriceProductByBrand() {
        List<LowestProductByBrandProjection> lowestPriceProductByBrand = productRepository.findLowestPriceProductByBrand();
        if (lowestPriceProductByBrand.isEmpty()) {
            throw new ApiException(ErrorCode.BRAND_LOWEST_PRICE_LIST_ERROR);
        }
        Map<String, BigDecimal> priceMap = lowestPriceProductByBrand.stream()
            .collect(Collectors.groupingBy(
                LowestProductByBrandProjection::getBrandName,
                Collectors.reducing(
                    BigDecimal.ZERO,
                    LowestProductByBrandProjection::getProductPrice,
                    BigDecimal::add
                )
            ));
        String minKey = Collections.min(priceMap.entrySet(), Map.Entry.comparingByValue()).getKey();

        List<BrandProduct> list = lowestPriceProductByBrand.stream()
            .filter(product -> product.getBrandName().equals(minKey))
            .map(product -> BrandProduct.createBrandProduct(
                product.getProductName(),
                product.getCategoryName(),
                product.getProductPrice()))
            .toList();
        return LowestPriceBrandProductsResponse.createLowestPriceBrandProductsResponse(
            LowestPriceBrandProduct.createLowestPriceBrandProducts(minKey, list,
                priceMap.get(minKey))
        );
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "categoryMinMaxPrice", key = "#p0")
    public CategoryPriceRangeResponse getCategoryMinMaxPrices(
        String categoryName) {
        return CategoryPriceRangeResponse.createCategoryPriceRangeResponse(
            categoryName,
            getLowestPriceProductByCategory(categoryName),
            getHighestPriceProductByCategory(categoryName)
        );
    }

    private LowestPriceProduct getLowestPriceProductByCategory(String categoryName) {
        PriceByCategoryProjection lowestPriceByCategory = productRepository.findLowestPriceByCategory(
                categoryName)
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_LOWEST_PRICE_PRODUCT_ERROR));
        return LowestPriceProduct.createLowestPriceProduct(
            lowestPriceByCategory.getProductName(),
            lowestPriceByCategory.getBrandName(),
            lowestPriceByCategory.getProductPrice());
    }

    private HighestPriceProduct getHighestPriceProductByCategory(String categoryName) {
        PriceByCategoryProjection highestPriceByCategory = productRepository.findHighestPriceByCategory(
                categoryName)
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_HIGHEST_PRICE_PRODUCT_ERROR));
        return HighestPriceProduct.createHighestPriceProduct(
            highestPriceByCategory.getProductName(),
            highestPriceByCategory.getBrandName(),
            highestPriceByCategory.getProductPrice());
    }
}

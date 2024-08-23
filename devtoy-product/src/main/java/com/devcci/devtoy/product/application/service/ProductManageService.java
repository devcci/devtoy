package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.domain.brand.Brand;
import com.devcci.devtoy.product.domain.brand.BrandRepository;
import com.devcci.devtoy.product.domain.category.Category;
import com.devcci.devtoy.product.domain.category.CategoryRepository;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.domain.product.event.ProductAdditionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductBulkDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductModificationEvent;
import com.devcci.devtoy.product.web.dto.ProductAddRequest;
import com.devcci.devtoy.product.web.dto.ProductUpdateRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductManageService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ProductManageService(
        ProductRepository productRepository,
        BrandRepository brandRepository,
        CategoryRepository categoryRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
    }


    @Transactional
    public Product addProduct(ProductAddRequest request) {
        Brand brand = findBrandByName(request.brandName());
        Category category = findCategoryByName(request.categoryName());
        Product newProduct = Product.createProduct(
            request.productName(), request.price(), brand, category, request.description(), request.stockQuantity()
        );
        checkDuplicateProduct(request.productName(), brand.getName(), category.getName());
        Product product = productRepository.save(newProduct);
        eventPublisher.publishEvent(new ProductAdditionEvent(product.getId()));
        return product;
    }

    @Transactional
    public void updateProduct(ProductUpdateRequest request) {
        Product originProduct = fetchProduct(request.productId());
        Brand brand = findBrandByName(request.brandName());
        Category category = findCategoryByName(request.categoryName());
        Product updatedProduct = Product.createProduct(request.productName(), request.price(), brand, category, request.description(), request.stockQuantity());

        if (!isChanged(originProduct, updatedProduct)) {
            throw new ApiException(ErrorCode.PRODUCT_NOT_CHANGED);
        }
        if (!(originProduct.getBrand().equals(updatedProduct.getBrand()) &&
            originProduct.getCategory().equals(updatedProduct.getCategory()))
        ) {
            checkDuplicateProduct(updatedProduct.getName(), brand.getName(), category.getName());
        }
        originProduct.update(updatedProduct);
        eventPublisher.publishEvent(new ProductModificationEvent(originProduct.getId()));
    }


    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
        productRepository.deleteById(productId);
        eventPublisher.publishEvent(new ProductDeletionEvent(product.getId()));
    }

    @Transactional
    public void deleteProductsByBrandId(Long brandId) {
        List<Product> products = productRepository.findAllByBrandId(brandId);
        if (!products.isEmpty()) {
            productRepository.deleteAllInBatch(products);
            eventPublisher.publishEvent(
                new ProductBulkDeletionEvent(products.stream().map(Product::getId).toList()));
        }
    }

    private void checkDuplicateProduct(String productName, String brandName, String categoryName) {
        if (productRepository.existsByNameAndBrandNameAndCategoryName(productName, brandName, categoryName)) {
            throw new ApiException(ErrorCode.PRODUCT_DUPLICATE);
        }
    }

    private Product fetchProduct(Long productId) {
        return productRepository.findByIdFetchJoin(productId)
            .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Brand findBrandByName(String brandName) {
        return brandRepository.findByName(brandName)
            .orElseThrow(() -> new ApiException(ErrorCode.BRAND_NOT_FOUND));
    }

    private Category findCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
            .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private boolean isChanged(Product originProduct, Product changedProduct) {
        return !originProduct.equals(changedProduct);
    }
}

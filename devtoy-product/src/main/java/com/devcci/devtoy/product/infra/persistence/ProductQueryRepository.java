package com.devcci.devtoy.product.infra.persistence;


import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByBrandProjection;
import com.devcci.devtoy.product.infra.persistence.projection.LowestProductByCategoryProjection;
import com.devcci.devtoy.product.infra.persistence.projection.PriceByCategoryProjection;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface ProductQueryRepository {

    List<Product> findAllFetchJoin(Pageable pageable);

    Optional<Product> findByIdFetchJoin(Long productId);

    boolean existsByNameAndBrandNameAndCategoryName(String productName, String brandName, String categoryName);

    List<LowestProductByCategoryProjection> findLowestPriceProductByCategory();

    List<LowestProductByBrandProjection> findLowestPriceProductByBrand();

    Optional<PriceByCategoryProjection> findLowestPriceByCategory(
        String categoryName);

    Optional<PriceByCategoryProjection> findHighestPriceByCategory(
        String categoryName);
}

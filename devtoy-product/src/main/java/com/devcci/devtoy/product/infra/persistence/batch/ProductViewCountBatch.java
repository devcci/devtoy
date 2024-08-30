package com.devcci.devtoy.product.infra.persistence.batch;

import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.infra.redis.ProductViewRedisService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class ProductViewCountBatch {

    private final ProductRepository productRepository;
    private final ProductViewRedisService productViewRedisService;

    public ProductViewCountBatch(ProductRepository productRepository,
        ProductViewRedisService productViewRedisService) {
        this.productRepository = productRepository;
        this.productViewRedisService = productViewRedisService;
    }


    @Transactional
    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.MINUTES)
    public void mergeViewCountToDatabase() {
        Set<Long> topProducts = productViewRedisService.getTopProducts(5000);

        if (!topProducts.isEmpty()) {
            topProducts.forEach(pid -> {
                Double viewCount = productViewRedisService.getProductViewCount(pid);
                Product product = productRepository.findById(pid).orElseThrow();
                product.increaseViewCount(viewCount.longValue());
            });

            productViewRedisService.resetProductViewCount();
        }
    }
}

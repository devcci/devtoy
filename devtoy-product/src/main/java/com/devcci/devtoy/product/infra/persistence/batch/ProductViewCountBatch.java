package com.devcci.devtoy.product.infra.persistence.batch;

import com.devcci.devtoy.common.infra.redis.RedisTemplateService;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class ProductViewCountBatch {

    private final ProductRepository productRepository;
    private final RedisTemplateService redisTemplateService;

    public ProductViewCountBatch(ProductRepository productRepository, RedisTemplateService redisTemplateService) {
        this.productRepository = productRepository;
        this.redisTemplateService = redisTemplateService;
    }


    @Transactional
    @Scheduled(fixedRate = 60, timeUnit = TimeUnit.MINUTES)
    public void mergeViewCountToDatabase() {
        Set<Object> topProducts = redisTemplateService.getTopProducts(5000);

        if (!topProducts.isEmpty()) {
            topProducts.forEach(pid -> {
                Long productId = Long.parseLong(pid.toString());
                Double viewCount = redisTemplateService.getProductViewCount(productId);
                Product product = productRepository.findById(productId).orElseThrow();
                product.increaseViewCount(viewCount.longValue());
            });

            redisTemplateService.resetProductViewCount();
        }
    }
}

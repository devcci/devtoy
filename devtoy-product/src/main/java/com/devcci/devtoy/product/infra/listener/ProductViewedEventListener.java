package com.devcci.devtoy.product.infra.listener;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.product.domain.product.Product;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.domain.product.event.ProductViewEvent;
import com.devcci.devtoy.product.infra.redis.ProductViewRedisService;
import org.springframework.context.event.EventListener;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductViewedEventListener {

    private final ProductViewRedisService productViewRedisService;
    private final ProductRepository productRepository;

    public ProductViewedEventListener(ProductViewRedisService productViewRedisService,
        ProductRepository productRepository) {
        this.productViewRedisService = productViewRedisService;
        this.productRepository = productRepository;
    }

    @Async("asyncExecutor")
    @EventListener
    @Transactional
    public void onProductViewed(ProductViewEvent event) {
        try {
            productViewRedisService.saveProductViewCount(event.productId());
        } catch (RedisConnectionFailureException | RedisSystemException | QueryTimeoutException e) {
            Product product = productRepository.findByIdWithPessimisticLock(event.productId())
                .orElseThrow(() -> new ApiException(ErrorCode.PRODUCT_NOT_FOUND));
            product.increaseViewCount();
        }
    }
}

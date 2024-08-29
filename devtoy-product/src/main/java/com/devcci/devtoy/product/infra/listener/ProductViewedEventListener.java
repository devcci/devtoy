package com.devcci.devtoy.product.infra.listener;

import com.devcci.devtoy.common.infra.redis.RedisTemplateService;
import com.devcci.devtoy.product.domain.product.event.ProductViewEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductViewedEventListener {

    private final RedisTemplateService redisTemplateService;

    public ProductViewedEventListener(RedisTemplateService redisTemplateService) {
        this.redisTemplateService = redisTemplateService;
    }

    @EventListener
    public void onProductViewed(ProductViewEvent event) {
        redisTemplateService.saveProductViewCount(event.productId());
    }
}

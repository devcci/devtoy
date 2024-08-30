package com.devcci.devtoy.product.infra.redis;

import com.devcci.devtoy.common.infra.redis.RedisKey;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProductViewRedisService {

    private final RedisTemplate<String, Long> productViewRedisTemplate;

    public ProductViewRedisService(
        RedisTemplate<String, Long> productViewRedisTemplate) {
        this.productViewRedisTemplate = productViewRedisTemplate;
    }

    public void saveProductViewCount(Long productId) {
        productViewRedisTemplate.opsForZSet().incrementScore(RedisKey.PRODUCT_VIEW_COUNT.getKey(), productId, 1);
    }

    public Double getProductViewCount(Long productId) {
        return productViewRedisTemplate.opsForZSet().score(RedisKey.PRODUCT_VIEW_COUNT.getKey(), productId);
    }

    public Set<Long> getTopProducts(long limit) {
        return productViewRedisTemplate.opsForZSet().reverseRange(RedisKey.PRODUCT_VIEW_COUNT.getKey(), 0, limit - 1);
    }

    public void resetProductViewCount() {
        productViewRedisTemplate.delete(RedisKey.PRODUCT_VIEW_COUNT.getKey());
    }
}

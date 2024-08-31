package com.devcci.devtoy.product.infra.redis;

import com.devcci.devtoy.common.infra.redis.RedisKey;
import com.devcci.devtoy.product.application.dto.ProductInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductInfoRedisService {

    private final RedisTemplate<String, ProductInfo> productInfoRedisTemplate;

    public ProductInfoRedisService(
        RedisTemplate<String, ProductInfo> productInfoRedisTemplate) {
        this.productInfoRedisTemplate = productInfoRedisTemplate;
    }

    public void save(String productId, ProductInfo value) {
        productInfoRedisTemplate.opsForValue()
            .set(RedisKey.PRODUCT_INFO.generate(productId), value, RedisKey.PRODUCT_INFO.getExpireTime(),
                RedisKey.PRODUCT_INFO.getTimeUnit());
    }

    public ProductInfo get(String productId) {
        return productInfoRedisTemplate.opsForValue().get(RedisKey.PRODUCT_INFO.generate(productId));
    }

    public void delete(String productId) {
        productInfoRedisTemplate.delete(RedisKey.PRODUCT_INFO.generate(productId));
    }

    public void deleteIn(Set<String> productIds) {
        Set<String> productInfoKeys = productIds.stream()
            .map(RedisKey.PRODUCT_INFO::generate)
            .collect(Collectors.toSet());
        productInfoRedisTemplate.delete(productInfoKeys);
    }
}

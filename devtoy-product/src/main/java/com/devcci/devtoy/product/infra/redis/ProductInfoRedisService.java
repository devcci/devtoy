package com.devcci.devtoy.product.infra.redis;

import com.devcci.devtoy.common.infra.redis.RedisKey;
import com.devcci.devtoy.product.application.dto.ProductInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductInfoRedisService {

    private final RedisTemplate<String, ProductInfo> productInfoRedisTemplate;

    public ProductInfoRedisService(
        RedisTemplate<String, ProductInfo> productInfoRedisTemplate) {
        this.productInfoRedisTemplate = productInfoRedisTemplate;
    }

    public void save(String key, ProductInfo value) {
        productInfoRedisTemplate.opsForValue()
            .set(RedisKey.PRODUCT_INFO.generate(key), value, RedisKey.PRODUCT_INFO.getExpireTime(),
                RedisKey.PRODUCT_INFO.getTimeUnit());
    }

    public ProductInfo get(String key) {
        return productInfoRedisTemplate.opsForValue().get(RedisKey.PRODUCT_INFO.generate(key));
    }

    public void delete(String key) {
        productInfoRedisTemplate.delete(RedisKey.PRODUCT_INFO.generate(key));
    }
}

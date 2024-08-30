package com.devcci.devtoy.product.infra.listener;

import com.devcci.devtoy.product.application.dto.ProductInfo;
import com.devcci.devtoy.product.domain.product.event.ProductViewEvent;
import com.devcci.devtoy.product.domain.product.event.ProductViewWithCachingEvent;
import com.devcci.devtoy.product.infra.redis.ProductInfoRedisService;
import com.devcci.devtoy.product.infra.redis.ProductViewRedisService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProductViewedEventListener {

    private final ProductViewRedisService productViewRedisService;
    private final ProductInfoRedisService productInfoRedisService;

    public ProductViewedEventListener(ProductViewRedisService productViewRedisService,
        ProductInfoRedisService productInfoRedisService) {
        this.productViewRedisService = productViewRedisService;
        this.productInfoRedisService = productInfoRedisService;
    }

    @EventListener
    public void onProductViewed(ProductViewEvent event) {
        productViewRedisService.saveProductViewCount(event.productId());

    }

    @EventListener
    public void onProductViewed(ProductViewWithCachingEvent event) {
        ProductInfo productInfo = event.productInfo();
        productViewRedisService.saveProductViewCount(productInfo.getProductId());
        productInfoRedisService.save(
            productInfo.getProductId().toString()
            , productInfo
        );
    }
}

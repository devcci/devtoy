package com.devcci.devtoy.product.infra.listener;


import com.devcci.devtoy.product.domain.product.event.ProductAdditionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductBulkDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductModificationEvent;
import com.devcci.devtoy.product.infra.cache.CacheRefreshHandler;
import com.devcci.devtoy.product.infra.redis.ProductInfoRedisService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductChangedEventListener {

    private final CacheRefreshHandler cacheRefreshHandler;
    private final ProductInfoRedisService productInfoRedisService;

    public ProductChangedEventListener(
        CacheRefreshHandler cacheRefreshHandler
        , ProductInfoRedisService productInfoRedisService
    ) {
        this.cacheRefreshHandler = cacheRefreshHandler;
        this.productInfoRedisService = productInfoRedisService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductDeleted(ProductDeletionEvent event) {
        cacheRefreshHandler.refreshProductCache();
        productInfoRedisService.delete(event.productId().toString());
    }

    @EventListener
    public void onProductBulkDeleted(ProductBulkDeletionEvent event) {
        cacheRefreshHandler.refreshProductCache();
        event.productIds().forEach(productId -> productInfoRedisService.delete(productId.toString()));
    }

    @EventListener
    public void onProductModified(ProductModificationEvent event) {
        cacheRefreshHandler.refreshProductCache();
        productInfoRedisService.delete(event.productId().toString());
    }

    @EventListener
    public void onProductAdded(ProductAdditionEvent event) {
        cacheRefreshHandler.refreshProductCache();
    }

}

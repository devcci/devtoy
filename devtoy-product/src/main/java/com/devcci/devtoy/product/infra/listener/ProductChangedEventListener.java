package com.devcci.devtoy.product.infra.listener;


import com.devcci.devtoy.product.domain.product.event.ProductAdditionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductBulkDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductDeletionEvent;
import com.devcci.devtoy.product.domain.product.event.ProductModificationEvent;
import com.devcci.devtoy.product.infra.cache.CacheRefreshHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductChangedEventListener {

    private final CacheRefreshHandler cacheRefreshHandler;

    public ProductChangedEventListener(
        CacheRefreshHandler cacheRefreshHandler
    ) {
        this.cacheRefreshHandler = cacheRefreshHandler;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductDeleted(ProductDeletionEvent event) {
        cacheRefreshHandler.clearProductCacheEntries();
        cacheRefreshHandler.evictProductInfoCache(event.productId());
    }

    @EventListener
    public void onProductBulkDeleted(ProductBulkDeletionEvent event) {
        cacheRefreshHandler.clearProductCacheEntries();
        event.productIds().forEach(cacheRefreshHandler::evictProductInfoCache);
    }

    @EventListener
    public void onProductModified(ProductModificationEvent event) {
        cacheRefreshHandler.clearProductCacheEntries();
        cacheRefreshHandler.evictProductInfoCache(event.productId());
    }

    @EventListener
    public void onProductAdded(ProductAdditionEvent ignoredEvent) {
        cacheRefreshHandler.clearProductCacheEntries();
    }

}

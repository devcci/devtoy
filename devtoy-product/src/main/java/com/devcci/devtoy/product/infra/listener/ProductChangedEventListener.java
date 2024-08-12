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

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onProductDeleted(ProductDeletionEvent event) {
        cacheRefreshHandler.refreshProductCache();
    }

    @EventListener
    public void onProductBulkDeleted(ProductBulkDeletionEvent event) {
        cacheRefreshHandler.refreshProductCache();
    }

    @EventListener
    public void onProductModified(ProductModificationEvent event) {
        cacheRefreshHandler.refreshProductCache();
    }

    @EventListener
    public void onProductAdded(ProductAdditionEvent event) {
        cacheRefreshHandler.refreshProductCache();
    }

}

package com.devcci.devtoy.product.infra.listener;


import com.devcci.devtoy.product.application.service.ProductManageService;
import com.devcci.devtoy.product.domain.brand.event.BrandAdditionEvent;
import com.devcci.devtoy.product.domain.brand.event.BrandDeletionEvent;
import com.devcci.devtoy.product.domain.brand.event.BrandModificationEvent;
import com.devcci.devtoy.product.infra.cache.CacheRefreshHandler;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class BrandChangedEventListener {

    private final ProductManageService productManageService;
    private final CacheRefreshHandler cacheRefreshHandler;

    public BrandChangedEventListener(
        ProductManageService productManageService,
        CacheRefreshHandler cacheRefreshHandler
    ) {
        this.productManageService = productManageService;
        this.cacheRefreshHandler = cacheRefreshHandler;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onBrandDeleted(BrandDeletionEvent event) {
        productManageService.deleteProductsByBrandId(event.brandId());
    }

    @EventListener
    public void onBrandModified(BrandModificationEvent ignoredEvent) {
        cacheRefreshHandler.clearProductCacheEntries();
    }

    @EventListener
    public void onBrandAdded(BrandAdditionEvent ignoredEvent) {
        cacheRefreshHandler.clearProductCacheEntries();
    }

}

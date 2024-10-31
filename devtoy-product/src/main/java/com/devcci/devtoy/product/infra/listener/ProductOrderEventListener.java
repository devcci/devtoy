package com.devcci.devtoy.product.infra.listener;

import com.devcci.devtoy.common.domain.OrderStatus;
import com.devcci.devtoy.common.infra.kafka.dto.OrderEventMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.domain.product.event.ProductOrderEvent;
import com.devcci.devtoy.product.infra.cache.CacheRefreshHandler;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class ProductOrderEventListener {

    private final OrderResultMessageProducer orderResultMessageProducer;
    private final CacheRefreshHandler cacheRefreshHandler;

    public ProductOrderEventListener(OrderResultMessageProducer orderResultMessageProducer
        , CacheRefreshHandler cacheRefreshHandler) {
        this.orderResultMessageProducer = orderResultMessageProducer;
        this.cacheRefreshHandler = cacheRefreshHandler;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductOrderEvent(ProductOrderEvent event) {
        OrderEventMessage orderEventMessage = event.orderEventMessage();
        orderResultMessageProducer.send(orderEventMessage.orderId().toString(),
            OrderResultMessage.of(orderEventMessage.orderId().toString(), OrderStatus.COMPLETED, null));

        cacheRefreshHandler.clearProductCacheEntries();
        orderEventMessage.orderProducts()
            .forEach(product -> cacheRefreshHandler.evictProductInfoCache(product.productId()));
    }
}


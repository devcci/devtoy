package com.devcci.devtoy.product.infra.listener;

import com.devcci.devtoy.common.domain.OrderStatus;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.domain.product.event.ProductOrderEvent;
import com.devcci.devtoy.product.infra.cache.CacheRefreshHandler;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import com.devcci.devtoy.product.infra.redis.ProductInfoRedisService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductOrderEventListener {

    private final OrderResultMessageProducer orderResultMessageProducer;
    private final ProductInfoRedisService productInfoRedisService;
    private final CacheRefreshHandler cacheRefreshHandler;

    public ProductOrderEventListener(OrderResultMessageProducer orderResultMessageProducer
        , ProductInfoRedisService productInfoRedisService
        , CacheRefreshHandler cacheRefreshHandler) {
        this.orderResultMessageProducer = orderResultMessageProducer;
        this.productInfoRedisService = productInfoRedisService;
        this.cacheRefreshHandler = cacheRefreshHandler;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductOrderEvent(ProductOrderEvent event) {
        OrderMessage orderMessage = event.orderMessage();
        orderResultMessageProducer.send(orderMessage.orderId().toString(),
            new OrderResultMessage(orderMessage.orderId().toString(), OrderStatus.COMPLETED.name(), null));

        cacheRefreshHandler.refreshProductCache();
        Set<String> productId = orderMessage.orderProducts().stream()
            .map(product -> product.productId().toString()).collect(Collectors.toSet());
        productInfoRedisService.deleteIn(productId);
    }
}


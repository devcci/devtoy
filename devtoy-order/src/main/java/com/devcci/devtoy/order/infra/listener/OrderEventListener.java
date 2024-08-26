package com.devcci.devtoy.order.infra.listener;

import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage.OrderProductMessage;
import com.devcci.devtoy.order.application.service.OrderService;
import com.devcci.devtoy.order.domain.order.Order;
import com.devcci.devtoy.order.domain.order.event.OrderCompletedEvent;
import com.devcci.devtoy.order.domain.order.event.OrderCreatedEvent;
import com.devcci.devtoy.order.domain.order.event.OrderFailedEvent;
import com.devcci.devtoy.order.infra.kafka.OrderKafkaProducer;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventListener {
    private final OrderKafkaProducer orderKafkaProducer;
    private final OrderService orderService;

    public OrderEventListener(OrderKafkaProducer orderKafkaProducer, OrderService orderService) {
        this.orderKafkaProducer = orderKafkaProducer;
        this.orderService = orderService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        Order order = event.order();
        OrderMessage orderMessage = OrderMessage.of(
            order.getId(),
            order.getMemberId(),
            order.getOrderProducts().stream()
                .map(orderProduct -> OrderProductMessage.of(
                    orderProduct.getProductId(),
                    orderProduct.getQuantity(),
                    orderProduct.getPrice()
                ))
                .toList()
        );
        orderKafkaProducer.send(order.getId().toString(), orderMessage);
    }

    @EventListener
    public void onOrderCompleted(OrderCompletedEvent event) {
        orderService.completeOrder(event.orderId());
    }

    @EventListener
    public void onOrderFailed(OrderFailedEvent event) {
        orderService.cancelOrder(event.orderId(), event.reason());
    }
}

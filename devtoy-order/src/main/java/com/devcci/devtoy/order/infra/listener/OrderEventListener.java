package com.devcci.devtoy.order.infra.listener;

import com.devcci.devtoy.order.domain.order.event.OrderCreatedEvent;
import com.devcci.devtoy.order.infra.kafka.OrderKafkaProducer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventListener {
    private final OrderKafkaProducer orderKafkaProducer;

    public OrderEventListener(OrderKafkaProducer orderKafkaProducer) {
        this.orderKafkaProducer = orderKafkaProducer;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        orderKafkaProducer.send(event.memberId(), event.orderRequests());

    }
}

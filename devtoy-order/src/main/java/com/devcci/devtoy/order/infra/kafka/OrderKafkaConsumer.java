package com.devcci.devtoy.order.infra.kafka;

import com.devcci.devtoy.common.domain.OrderStatus;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.order.domain.order.event.OrderCompletedEvent;
import com.devcci.devtoy.order.domain.order.event.OrderFailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaConsumer implements AcknowledgingMessageListener<String, OrderResultMessage> {

    private final ApplicationEventPublisher eventPublisher;

    public OrderKafkaConsumer(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    @KafkaListener(topics = "${topic.order.result}", containerFactory = "orderResultMessageListenerFactory")
    public void onMessage(@NonNull ConsumerRecord<String, OrderResultMessage> msg, Acknowledgment acknowledgment) {
        try {
            if (msg.value().status() == OrderStatus.COMPLETED) {
                eventPublisher.publishEvent(new OrderCompletedEvent(Long.valueOf(msg.value().orderId())));
            } else {
                eventPublisher.publishEvent(
                    new OrderFailedEvent(Long.valueOf(msg.value().orderId()), msg.value().reason()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            eventPublisher.publishEvent(
                new OrderFailedEvent(Long.valueOf(msg.value().orderId()), msg.value().reason()));
        } finally {
            acknowledgment.acknowledge();
        }
    }
}

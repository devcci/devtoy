package com.devcci.devtoy.order.infra.kafka;

import com.devcci.devtoy.common.infra.kafka.dto.OrderEventMessage;
import com.devcci.devtoy.order.domain.order.event.OrderFailedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class OrderKafkaProducer {

    private final KafkaTemplate<String, OrderEventMessage> orderKafkaTemplate;
    private final String topicOrderEvent;
    private final ApplicationEventPublisher eventPublisher;

    public OrderKafkaProducer(
        @Qualifier("orderKafkaTemplate") KafkaTemplate<String, OrderEventMessage> orderKafkaTemplate,
        @Value("${topic.order.event}") String topicOrderEvent, ApplicationEventPublisher eventPublisher
    ) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.topicOrderEvent = topicOrderEvent;
        this.eventPublisher = eventPublisher;
    }

    public void send(String orderId, OrderEventMessage msg) {
        CompletableFuture<SendResult<String, OrderEventMessage>> send = orderKafkaTemplate.send(topicOrderEvent,
            orderId,
            msg);

        send.whenComplete((result, exception) -> {
            if (exception == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("메시지 발행 성공: Partition= {}, Offset= {}", metadata.partition(), metadata.offset());
            } else {
                log.error("메시지 발행 실패: {}", exception.getMessage());
                eventPublisher.publishEvent(
                    new OrderFailedEvent(msg.orderId(), "Kafka 장애 발생"));
            }
        });
    }
}

package com.devcci.devtoy.product.infra.kafka;

import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.infra.persistence.outbox.OutboxMessage;
import com.devcci.devtoy.product.infra.persistence.outbox.OutboxMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class OrderResultMessageProducer {

    private final KafkaTemplate<String, OrderResultMessage> orderResultKafkaTemplate;
    private final String topic;
    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;

    public OrderResultMessageProducer(
        @Qualifier("orderResultKafkaTemplate") KafkaTemplate<String, OrderResultMessage> orderResultKafkaTemplate,
        @Value("${topic.order.result}") String topic,
        OutboxMessageRepository outboxMessageRepository, ObjectMapper objectMapper
    ) {
        this.orderResultKafkaTemplate = orderResultKafkaTemplate;
        this.topic = topic;
        this.outboxMessageRepository = outboxMessageRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public CompletableFuture<SendResult<String, OrderResultMessage>> send(String orderId, OrderResultMessage msg) {
        return orderResultKafkaTemplate.send(topic, orderId, msg).whenComplete((result, exception) -> {
            if (exception == null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("메시지 발행 성공: Partition= {}, Offset= {}", metadata.partition(), metadata.offset());
            } else {
                log.error("메시지 발행 실패: {}", exception.getMessage());
                try {
                    String jsonMsg = objectMapper.writeValueAsString(msg);
                    OutboxMessage orderOutboxMsg = OutboxMessage.createOutboxMessage(msg.getClass().getSimpleName(),
                        orderId, jsonMsg);
                    outboxMessageRepository.save(orderOutboxMsg);
                } catch (JsonProcessingException e) {
                    log.error("Failed to serialize message: {}", msg);
                }
            }
        });
    }

    @Transactional
    public void sendForRelay(String orderId,
        OrderResultMessage msg) {
        orderResultKafkaTemplate.send(topic, orderId, msg).whenComplete((result, exception) -> {
            if (exception == null) {
                outboxMessageRepository.findByAggregateTypeAndAggregateId(msg.getClass().getSimpleName(), orderId)
                    .ifPresentOrElse(OutboxMessage::markAsSent
                        , () -> log.error("Outbox 메시지를 찾을 수 없음: {}", msg)
                    );
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Outbox 메시지 발행 성공: Partition= {}, Offset= {}", metadata.partition(), metadata.offset());
            } else {
                log.error("Outbox 메시지 발행 실패: {}", exception.getMessage());
            }
        });
    }
}

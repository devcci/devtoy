package com.devcci.devtoy.product.infra.persistence.batch;

import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import com.devcci.devtoy.product.infra.persistence.outbox.OutboxMessageRepository;
import com.devcci.devtoy.product.infra.persistence.outbox.OutboxMessageStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Component
public class OutboxMessageRelay {

    private final OutboxMessageRepository outboxMessageRepository;
    private final OrderResultMessageProducer orderResultMessageProducer;
    private final ObjectMapper objectMapper;

    public OutboxMessageRelay(OutboxMessageRepository outboxMessageRepository,
        OrderResultMessageProducer orderResultMessageProducer,
        ObjectMapper objectMapper) {
        this.outboxMessageRepository = outboxMessageRepository;
        this.orderResultMessageProducer = orderResultMessageProducer;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void relayOutboxMessages() {
        System.out.println("Relaying outbox messages");
        outboxMessageRepository.findByStatus(OutboxMessageStatus.PENDING).forEach(outboxMessage -> {
            try {
                OrderResultMessage orderResultMessage = objectMapper.readValue(outboxMessage.getPayload(),
                    OrderResultMessage.class);
                orderResultMessageProducer.sendForRelay(outboxMessage.getAggregateId(), orderResultMessage);
                outboxMessage.incrementAttempts();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

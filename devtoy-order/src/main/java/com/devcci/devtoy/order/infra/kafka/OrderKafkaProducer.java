package com.devcci.devtoy.order.infra.kafka;

import com.devcci.devtoy.order.web.dto.OrderRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "kafka.enabled", matchIfMissing = true)
@Component
public class OrderKafkaProducer {
    private final KafkaTemplate<String, Object> orderKafkaTemplate;
    private final String topicOrderCreate;
    private final ObjectMapper objectMapper;

    public OrderKafkaProducer(
        @Qualifier("orderKafkaTemplate") KafkaTemplate<String, Object> orderKafkaTemplate,
        @Value("${topic.order.create}") String topicOrderCreate,
        ObjectMapper objectMapper
    ) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.topicOrderCreate = topicOrderCreate;
        this.objectMapper = objectMapper;
    }

    public void send(Long memberId, OrderRequest orderRequest) {
        String jsonOrderRequest;
        try {
            jsonOrderRequest = objectMapper.writeValueAsString(orderRequest);
            orderKafkaTemplate.send(topicOrderCreate, memberId.toString(), jsonOrderRequest);
            orderKafkaTemplate.flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}

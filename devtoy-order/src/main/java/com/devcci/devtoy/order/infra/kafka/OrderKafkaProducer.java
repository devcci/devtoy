package com.devcci.devtoy.order.infra.kafka;

import com.devcci.devtoy.order.infra.kafka.dto.OrderMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaProducer {
    private final KafkaTemplate<String, OrderMessage> orderKafkaTemplate;
    private final String topicOrderCreate;

    public OrderKafkaProducer(
        @Qualifier("orderKafkaTemplate") KafkaTemplate<String, OrderMessage> orderKafkaTemplate,
        @Value("${topic.order.create}") String topicOrderCreate
    ) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.topicOrderCreate = topicOrderCreate;
    }

    public void send(String orderId, OrderMessage msg) {
        orderKafkaTemplate.send(topicOrderCreate, orderId, msg);
    }
}

package com.devcci.devtoy.product.infra.kafka;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderKafkaProducer {
    private final KafkaTemplate<String, Object> orderKafkaTemplate;
    private final String topic;

    public OrderKafkaProducer(
        @Qualifier("orderKafkaTemplate") KafkaTemplate<String, Object> orderKafkaTemplate,
        @Value("${topic.order.fail}") String topic
    ) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.topic = topic;
    }

    public void send(String message) {
        orderKafkaTemplate.send(topic, message);
    }
}

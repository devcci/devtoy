package com.devcci.devtoy.product.infra.kafka;

import com.devcci.devtoy.product.infra.kafka.dto.OrderResultMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderResultMessageProducer {
    private final KafkaTemplate<String, OrderResultMessage> orderResultKafkaTemplate;
    private final String topic;

    public OrderResultMessageProducer(
        @Qualifier("orderResultKafkaTemplate") KafkaTemplate<String, OrderResultMessage> orderResultKafkaTemplate,
        @Value("${topic.order.result}") String topic
    ) {
        this.orderResultKafkaTemplate = orderResultKafkaTemplate;
        this.topic = topic;
    }

    public void send(String orderId, OrderResultMessage msg) {
        orderResultKafkaTemplate.send(topic, orderId, msg);
    }
}

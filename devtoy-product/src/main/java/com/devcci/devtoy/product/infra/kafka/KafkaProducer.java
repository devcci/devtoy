package com.devcci.devtoy.product.infra.kafka;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "kafka.enabled", matchIfMissing = true)
@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Object> productKafkaTemplate;
    private final String topic;

    public KafkaProducer(
        @Qualifier("productKafkaTemplate") KafkaTemplate<String, Object> productKafkaTemplate,
        @Value("${kafka.product.topic.delete}") String topic
    ) {
        this.productKafkaTemplate = productKafkaTemplate;
        this.topic = topic;
    }

    public void send(String message) {
        productKafkaTemplate.send(topic, message);
    }
}

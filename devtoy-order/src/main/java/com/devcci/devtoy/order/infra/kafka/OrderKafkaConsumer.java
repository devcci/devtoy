package com.devcci.devtoy.order.infra.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderKafkaConsumer implements AcknowledgingMessageListener<String, String> {

    @Override
    @KafkaListener(topics = "${topic.order.fail}", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord<String, String> data, Acknowledgment acknowledgment) {
        try {
            log.info("topic: {} key: {} value: {}", data.topic(), data.key(), data.value());
            acknowledgment.acknowledge();
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }
}

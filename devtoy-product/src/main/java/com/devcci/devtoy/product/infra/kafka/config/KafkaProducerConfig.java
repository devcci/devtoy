package com.devcci.devtoy.product.infra.kafka.config;

import com.devcci.devtoy.common.infra.kafka.config.CommonKafkaProducerConfig;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig extends CommonKafkaProducerConfig {
    private final String kafkaBootstrapServers;

    public KafkaProducerConfig(
        @Value("${spring.kafka.bootstrap-servers}")
        String kafkaBootstrapServers
    ) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
    }

    @Bean("orderResultKafkaTemplate")
    public KafkaTemplate<String, OrderResultMessage> orderResultKafkaTemplate() {
        return kafkaTemplate(kafkaBootstrapServers, producerFactory(), null);
    }

}

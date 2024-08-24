package com.devcci.devtoy.order.infra.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public KafkaAdmin.NewTopics newTopics() {
        String devtoyOrderCreate = "devtoy-order-create";
        String devtoyOrderFail = "devtoy-order-fail";
        int partitionCount = 3;
        int replicaCount = 3;
        return new KafkaAdmin.NewTopics(
            TopicBuilder.name(devtoyOrderCreate)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build(),

            TopicBuilder.name(devtoyOrderFail)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build()
        );
    }
}

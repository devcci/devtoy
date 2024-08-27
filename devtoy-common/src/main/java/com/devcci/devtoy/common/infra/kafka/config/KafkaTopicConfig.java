package com.devcci.devtoy.common.infra.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Profile("!integrationTest")
@Configuration
public class KafkaTopicConfig {
    @Bean
    public KafkaAdmin.NewTopics newTopics() {
        String devtoyOrderCreate = "devtoy-order-create";
        String devtoyOrderResult = "devtoy-order-result";
        int partitionCount = 3;
        int replicaCount = 3;
        return new KafkaAdmin.NewTopics(
            TopicBuilder.name(devtoyOrderCreate)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build(),

            TopicBuilder.name(devtoyOrderResult)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build()
        );
    }
}

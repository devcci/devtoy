package com.devcci.devtoy.product.infra.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    private final String kafkaBootstrapServers;
    private final TaskExecutorConfig taskExecutorConfig;

    public KafkaConsumerConfig(@Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers, TaskExecutorConfig taskExecutorConfig) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.taskExecutorConfig = taskExecutorConfig;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 2000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "devtoy-order-create-group");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        // 컨슈머 쓰레드 개수를 파티션에 맞춰 3개로 해뒀지만 만약 Scale-out을 하게되면 파티션이 다른 컨슈머 프로세스에 사용되어 쓰레드가 낭비될 수 있다.
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setListenerTaskExecutor(taskExecutorConfig.executor());
        factory.setCommonErrorHandler(retryHandler());
        return factory;
    }

    private DefaultErrorHandler retryHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) ->
            log.error("{} consume Failure. cause: {} message key: {} message value: {}", consumerRecord.topic(), e.getMessage(), consumerRecord.key(), consumerRecord.value()),
            new FixedBackOff(1000L, 5L));
        errorHandler.addNotRetryableExceptions(IllegalAccessException.class);
        return errorHandler;
    }
}

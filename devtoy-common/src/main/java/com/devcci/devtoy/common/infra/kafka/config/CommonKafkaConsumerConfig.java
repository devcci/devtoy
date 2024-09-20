package com.devcci.devtoy.common.infra.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommonKafkaConsumerConfig {

    private final String kafkaBootstrapServers;
    private final ThreadPoolTaskExecutor kafkaExecutor;

    public CommonKafkaConsumerConfig(
        String kafkaBootstrapServers,
        ThreadPoolTaskExecutor kafkaExecutor
    ) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.kafkaExecutor = kafkaExecutor;
    }

    public Map<String, Object> consumerConfigs(String consumerGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 2000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, 5000);
        props.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, 10000);
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 10000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 15000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        return props;
    }

    public <K, V> KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory(
        ConsumerFactory<K, V> consumerFactory,
        int concurrency
    ) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 컨슈머 쓰레드 개수를 파티션에 맞춰 3개로 해뒀지만 만약 Scale-out을 하게되면 파티션이 다른 컨슈머 프로세스에 사용되어 쓰레드가 낭비될 수 있다.
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setListenerTaskExecutor(kafkaExecutor);
        factory.setCommonErrorHandler(retryHandler());
        return factory;
    }

    private DefaultErrorHandler retryHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) ->
            log.error("{} consume Failure. cause: {} message key: {} message value: {}", consumerRecord.topic(),
                e.getMessage(), consumerRecord.key(), consumerRecord.value()),
            new FixedBackOff(3000L, 3L));
        errorHandler.addNotRetryableExceptions(IllegalAccessException.class);
        return errorHandler;
    }
}

package com.devcci.devtoy.order.infra.kafka.config;

import com.devcci.devtoy.order.infra.kafka.dto.OrderResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    private final String kafkaBootstrapServers;
    private final ThreadPoolTaskExecutor kafkaExecutor;

    public KafkaConsumerConfig(
        @Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers,
        @Qualifier("kafkaExecutor") ThreadPoolTaskExecutor kafkaExecutor
    ) {
        this.kafkaBootstrapServers = kafkaBootstrapServers;
        this.kafkaExecutor = kafkaExecutor;
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 2000);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "devtoy-order-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        return props;
    }

    public <K, V> KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory(
        ConsumerFactory<K, V> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 컨슈머 쓰레드 개수를 파티션에 맞춰 3개로 해뒀지만 만약 Scale-out을 하게되면 파티션이 다른 컨슈머 프로세스에 사용되어 쓰레드가 낭비될 수 있다.
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setListenerTaskExecutor(kafkaExecutor);
        factory.setCommonErrorHandler(retryHandler());
        return factory;
    }

    @Bean("orderResultMessageListenerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderResultMessage>> orderMessageListenerFactory() {
        JsonDeserializer<OrderResultMessage> jsonDeserializer = new JsonDeserializer<>(OrderResultMessage.class);
        jsonDeserializer.setUseTypeHeaders(false);

        ConsumerFactory<String, OrderResultMessage> consumerFactory =
            new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), jsonDeserializer);

        return kafkaListenerContainerFactory(consumerFactory);
    }

    private DefaultErrorHandler retryHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) ->
            log.error("{} consume Failure. cause: {} message key: {} message value: {}", consumerRecord.topic(), e.getMessage(), consumerRecord.key(), consumerRecord.value()),
            new FixedBackOff(1000L, 2L));
        errorHandler.addNotRetryableExceptions(IllegalAccessException.class);
        return errorHandler;
    }
}

package com.devcci.devtoy.order.infra.kafka.config;

import com.devcci.devtoy.common.infra.kafka.config.CommonKafkaConsumerConfig;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class KafkaConsumerConfig extends CommonKafkaConsumerConfig {
    private final String consumerGroupId;

    public KafkaConsumerConfig(
        @Value("${spring.kafka.bootstrap-servers}") String kafkaBootstrapServers,
        @Qualifier("asyncExecutor") ThreadPoolTaskExecutor kafkaExecutor,
        @Value("${spring.kafka.consumer.group-id}") String consumerGroupId
    ) {
        super(kafkaBootstrapServers, kafkaExecutor);
        this.consumerGroupId = consumerGroupId;
    }

    @Bean("orderResultMessageListenerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderResultMessage>> orderMessageListenerFactory() {
        JsonDeserializer<OrderResultMessage> jsonDeserializer = new JsonDeserializer<>(OrderResultMessage.class);
        jsonDeserializer.setUseTypeHeaders(false);

        ConsumerFactory<String, OrderResultMessage> consumerFactory =
            new DefaultKafkaConsumerFactory<>(consumerConfigs(consumerGroupId), new StringDeserializer(), jsonDeserializer);

        return kafkaListenerContainerFactory(consumerFactory, 3);
    }

}

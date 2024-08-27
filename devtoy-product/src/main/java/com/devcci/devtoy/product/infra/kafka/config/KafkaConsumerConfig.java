package com.devcci.devtoy.product.infra.kafka.config;

import com.devcci.devtoy.common.infra.kafka.config.CommonKafkaConsumerConfig;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Profile("!integrationTest")
@EnableKafka
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

    @Bean("orderCreatedMessageListenerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderMessage>> orderMessageListenerFactory() {
        JsonDeserializer<OrderMessage> jsonDeserializer = new JsonDeserializer<>(OrderMessage.class);
        jsonDeserializer.setUseTypeHeaders(false);

        ConsumerFactory<String, OrderMessage> consumerFactory =
            new DefaultKafkaConsumerFactory<>(consumerConfigs(consumerGroupId), new StringDeserializer(), jsonDeserializer);

        return kafkaListenerContainerFactory(consumerFactory, 3);
    }

}

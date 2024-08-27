package com.devcci.devtoy.order.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@TestConfiguration
public class TestContainerConfig {

    @Bean
    @ServiceConnection("mysql")
    public MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.4.2")
            .withDatabaseName("devtoy-order-test")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort());
    }

    // https://java.testcontainers.org/modules/kafka/
    // 문제가 있다.. 'KafkaContainer(java. lang. String)' is deprecated 이거때문에 신규 메서드 사용하려했더니 의존성 지원이 이상하다
    // confluentinc/cp-kafka 이미지 사용하려면 org.testcontainers.kafka.ConfluentKafkaContainer 패키지 사용하라는데 얘가 없다;
    // 일단 지금은 문제 없으니 대기

    @Bean
    @ServiceConnection("kafka")
    public KafkaContainer kafkaContainer() {
        return new KafkaContainer("7.7.0")
            .waitingFor(Wait.forListeningPort());
    }

}

package com.devcci.devtoy.product.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@TestConfiguration
public class TestContainerConfig {

    @Bean
    @ServiceConnection("mysql")
    public MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.4.2")
            .withDatabaseName("devtoy-product-test")
            .withUsername("test")
            .withPassword("test")
            .waitingFor(Wait.forListeningPort());
    }

    @Bean
    @ServiceConnection("redis")
    public GenericContainer<?> redisContainer() {
        GenericContainer redisContainer = new GenericContainer("redis:7.4");
        redisContainer.withExposedPorts(6379);
        redisContainer.waitingFor(Wait.forListeningPort());
        redisContainer.start();
        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redisContainer.getMappedPort(6379)));
        return redisContainer;
    }

    // https://java.testcontainers.org/modules/kafka/
    // 문제가 있다.. 'KafkaContainer(java. lang. String)' is deprecated 이거때문에 신규 메서드 사용하려했더니 의존성 지원이 이상하다
    // confluentinc/cp-kafka 이미지 사용하려면 org.testcontainers.kafka.ConfluentKafkaContainer 패키지 사용하라는데 얘가 없다;
    // 일단 지금은 문제 없으니 대기
    @Bean
    @ServiceConnection("kafka")
    public KafkaContainer kafkaContainer() {
        KafkaContainer kafkaContainer = new KafkaContainer("7.7.0");
        kafkaContainer
            .waitingFor(Wait.forListeningPort());
        kafkaContainer.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafkaContainer.getBootstrapServers());
        return kafkaContainer;
    }

}

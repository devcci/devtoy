package com.devcci.devtoy.product.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
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
        return new GenericContainer<>("redis:7.4")
            .withExposedPorts(6379)
            .waitingFor(Wait.forListeningPort());
    }

}

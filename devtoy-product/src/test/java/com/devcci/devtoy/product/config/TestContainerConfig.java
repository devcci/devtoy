package com.devcci.devtoy.product.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

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

    //  ServiceConnection으로 실행하면 호스트를 제대로 찾았다가 못찾았다 계속 바뀐다.. 예전 방식으로 대체함
    static {
        GenericContainer<?> redis =
            new GenericContainer<>(DockerImageName.parse("redis:7.4")).withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.port", redis.getMappedPort(6379).toString());
    }

}

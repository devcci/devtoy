package com.devcci.devtoy.member.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration
public class TestContainerConfig {


    @Bean
    @ServiceConnection
    public MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.4.2")
            .withDatabaseName("devtoy-member-test")
            .withUsername("test")
            .withPassword("test");
    }
}

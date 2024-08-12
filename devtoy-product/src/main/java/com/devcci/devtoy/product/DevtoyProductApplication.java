package com.devcci.devtoy.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class DevtoyProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtoyProductApplication.class, args);
    }

}

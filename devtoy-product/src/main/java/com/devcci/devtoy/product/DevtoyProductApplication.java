package com.devcci.devtoy.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.devcci.devtoy"})
@SpringBootApplication
public class DevtoyProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtoyProductApplication.class, args);
    }

}

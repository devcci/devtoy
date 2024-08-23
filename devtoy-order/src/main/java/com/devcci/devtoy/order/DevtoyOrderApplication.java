package com.devcci.devtoy.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.devcci.devtoy"})
@SpringBootApplication
public class DevtoyOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtoyOrderApplication.class, args);
    }

}

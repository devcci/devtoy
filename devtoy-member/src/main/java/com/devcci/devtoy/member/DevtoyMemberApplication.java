package com.devcci.devtoy.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.devcci.devtoy"})
@SpringBootApplication
public class DevtoyMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtoyMemberApplication.class, args);
    }

}

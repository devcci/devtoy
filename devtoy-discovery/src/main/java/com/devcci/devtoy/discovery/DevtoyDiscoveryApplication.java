package com.devcci.devtoy.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DevtoyDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtoyDiscoveryApplication.class, args);
    }

}

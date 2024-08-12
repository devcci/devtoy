package com.devcci.devtoy.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DevtoyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevtoyGatewayApplication.class, args);
    }

}

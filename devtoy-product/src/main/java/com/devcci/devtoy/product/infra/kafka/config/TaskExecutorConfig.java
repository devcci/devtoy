package com.devcci.devtoy.product.infra.kafka.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@ConditionalOnProperty(value = "kafka.enabled", matchIfMissing = true)
@Configuration
public class TaskExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(200);
        executor.setQueueCapacity(250);
        executor.setThreadFactory(new CustomizableThreadFactory("kafka-thread")); // 이름 prefix
        return executor;
    }
}
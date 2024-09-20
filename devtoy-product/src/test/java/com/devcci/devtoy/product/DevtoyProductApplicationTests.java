package com.devcci.devtoy.product;

import com.devcci.devtoy.common.domain.OrderStatus;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.config.IntegrationTest;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class DevtoyProductApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderResultMessageProducer orderResultMessageProducer;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void testKafkaProducer() throws InterruptedException, ExecutionException {
        OrderResultMessage message = new OrderResultMessage("1", OrderStatus.COMPLETED, null);
        SendResult<String, OrderResultMessage> result = orderResultMessageProducer.send("1", message).get();
        assertThat(result.getProducerRecord().value()).isEqualTo(message);
    }

    @Test
    void testDatabaseConnection() {
        String query = "SELECT 1";
        Integer result = jdbcTemplate.queryForObject(query, Integer.class);
        assertThat(result).isEqualTo(1);
    }

    @Test
    void testRedisTemplateService() {
        String key = "test";
        String value = "test";
        redisTemplate.opsForValue().set(key, value, 100L, TimeUnit.SECONDS);
        String retrievedValue = redisTemplate.opsForValue().get(key);
        assertThat(retrievedValue).isEqualTo(value);
    }
}

package com.devcci.devtoy.product;

import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.config.IntegrationTest;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.support.SendResult;
import org.testcontainers.containers.KafkaContainer;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class DevtoyProductApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OrderResultMessageProducer orderResultMessageProducer;
    @Autowired
    private KafkaContainer kafkaContainer;

    @Test
    void testKafkaProducer() throws InterruptedException, ExecutionException {
        OrderResultMessage message = new OrderResultMessage("1", "COMPLETED", null);
        SendResult<String, OrderResultMessage> result = orderResultMessageProducer.send("1", message).get();
        assertThat(result.getProducerRecord().value()).isEqualTo(message);
    }

    @Test
    void testDatabaseConnection() {
        String query = "SELECT 1";
        Integer result = jdbcTemplate.queryForObject(query, Integer.class);
        assertThat(result).isEqualTo(1);
    }
}

package com.devcci.devtoy.product.application.service;

import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.config.IntegrationTest;
import com.devcci.devtoy.product.domain.product.ProductRepository;
import com.devcci.devtoy.product.infra.kafka.OrderResultMessageProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
public class ProductStockManageServiceIntegrationTest {
    @Autowired
    private ProductStockManageService productStockManageService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderResultMessageProducer orderResultMessageProducer;


    @DisplayName("재고 수량 수정")
    @Test
    void modifyStockQuantity() {
        // given
        Long productId = 1L;
        Long quantity = 50L;

        // when
        productStockManageService.modifyStockQuantity(productId, quantity);

        // then
        assertThat(productRepository.findById(productId).orElseThrow().getStockQuantity()).isEqualTo(50L);
    }

    @DisplayName("재고 수량 차감 - 비관적 락 적용")
    @Test
    void removeStockQuantity() throws ExecutionException, InterruptedException {
        // given
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<OrderMessage> orders = List.of(
            OrderMessage.of(1L, "tester1",
                List.of(OrderMessage.OrderProductMessage.of(1L, 1L, new BigDecimal("11200"))))
        );
        OrderResultMessage message = new OrderResultMessage("1", "COMPLETED", null);

        Runnable task = () -> {
            try {
                readyLatch.countDown();
                startLatch.await();
                productStockManageService.removeStockQuantity(orders.get(0));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                doneLatch.countDown();
            }
        };

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(task);
        }
        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executorService.shutdown();

        // then
        assertThat(productRepository.findById(1L).orElseThrow().getStockQuantity()).isEqualTo(50L);
        SendResult<String, OrderResultMessage> result = orderResultMessageProducer.send("1", message).get();
        assertThat(result.getProducerRecord().value().orderId()).isEqualTo("1");
    }

}

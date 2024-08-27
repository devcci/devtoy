package com.devcci.devtoy.product.infra.kafka;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.infra.kafka.dto.OrderMessage;
import com.devcci.devtoy.common.infra.kafka.dto.OrderResultMessage;
import com.devcci.devtoy.product.application.service.ProductStockManageService;
import com.devcci.devtoy.product.infra.kafka.config.KafkaConsumerConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@ConditionalOnBean(KafkaConsumerConfig.class)
@Slf4j
@Component
public class OrderMessageConsumer implements AcknowledgingMessageListener<String, OrderMessage> {
    private final ProductStockManageService productStockManageService;
    private final OrderResultMessageProducer orderResultMessageProducer;

    public OrderMessageConsumer(ProductStockManageService productStockManageService, OrderResultMessageProducer orderResultMessageProducer) {
        this.productStockManageService = productStockManageService;
        this.orderResultMessageProducer = orderResultMessageProducer;
    }

    @Override
    @KafkaListener(topics = "${topic.order.create}", containerFactory = "orderCreatedMessageListenerFactory")
    public void onMessage(ConsumerRecord<String, OrderMessage> msg, Acknowledgment acknowledgment) {
        try {
            productStockManageService.removeStockQuantity(msg.value());
            acknowledgment.acknowledge();
        } catch (ApiException e) {
            OrderResultMessage orderResultMessage =
                new OrderResultMessage(msg.value().orderId().toString(), "FAILED", e.getErrorCode().getMessage());
            orderResultMessageProducer.send(orderResultMessage.orderId(), orderResultMessage);
            acknowledgment.acknowledge();
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }
}

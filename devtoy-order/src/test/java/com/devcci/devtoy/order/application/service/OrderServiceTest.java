package com.devcci.devtoy.order.application.service;

import com.devcci.devtoy.common.domain.OrderStatus;
import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.order.config.UnitTest;
import com.devcci.devtoy.order.domain.order.Order;
import com.devcci.devtoy.order.domain.order.OrderRepository;
import com.devcci.devtoy.order.domain.order.event.OrderCreatedEvent;
import com.devcci.devtoy.order.infra.client.ProductBulkResponse;
import com.devcci.devtoy.order.infra.client.ProductFeignClient;
import com.devcci.devtoy.order.web.dto.OrderRequest;
import com.devcci.devtoy.order.web.dto.OrderRequest.OrderProductRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@UnitTest
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductFeignClient productFeignClient;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder() {
        // given
        String memberId = "tester1";
        OrderProductRequest productRequest1 = new OrderProductRequest(1L, 2L);
        OrderProductRequest productRequest2 = new OrderProductRequest(2L, 3L);
        OrderRequest orderRequest = new OrderRequest(List.of(productRequest1, productRequest2));

        ProductBulkResponse product1 = new ProductBulkResponse(1L, BigDecimal.valueOf(100), 100L);
        ProductBulkResponse product2 = new ProductBulkResponse(2L, BigDecimal.valueOf(200), 100L);

        Order order = Order.createOrder(memberId, OrderStatus.CREATED);

        given(productFeignClient.getProductsByIds(Set.of(1L, 2L))).willReturn(List.of(product1, product2));
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        orderService.createOrder(memberId, orderRequest);

        // then
        verify(orderRepository).save(any(Order.class));
        verify(eventPublisher).publishEvent(any(OrderCreatedEvent.class));
    }

    @Test
    void createOrderProductNotFound() {
        // given
        String memberId = "tester1";
        OrderProductRequest productRequest1 = new OrderProductRequest(1L, 2L);
        OrderProductRequest productRequest2 = new OrderProductRequest(2L, 3L);
        OrderRequest orderRequest = new OrderRequest(List.of(productRequest1, productRequest2));

        ProductBulkResponse product1 = new ProductBulkResponse(1L, BigDecimal.valueOf(100), 100L);

        given(productFeignClient.getProductsByIds(Set.of(1L, 2L))).willReturn(List.of(product1));

        // when, then
        assertThatThrownBy(() -> orderService.createOrder(memberId, orderRequest))
            .isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.PRODUCT_LIST_NOT_LOADED.getMessage())
            .extracting("details")
            .isEqualTo("존재하지 않는 상품 ID: [2]");
    }

    @Test
    void completeOrder() {
        // given
        Long orderId = 1L;
        Order order = Order.createOrder("tester1", OrderStatus.CREATED);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when
        orderService.completeOrder(orderId);

        // then
        verify(orderRepository).findById(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    void completeOrderOrderNotFound() {
        // given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.completeOrder(orderId))
            .isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }

    @Test
    void cancelOrder() {
        // given
        Long orderId = 1L;
        String reason = "reason";
        Order order = Order.createOrder("tester1", OrderStatus.CREATED);
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when
        orderService.cancelOrder(orderId, reason);

        // then
        verify(orderRepository).findById(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    void cancelOrderOrderNotFound() {
        // given
        Long orderId = 1L;
        given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.cancelOrder(orderId, "reason"))
            .isInstanceOf(ApiException.class)
            .hasMessageContaining(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }
}

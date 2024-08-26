package com.devcci.devtoy.order.application.service;

import com.devcci.devtoy.common.exception.ApiException;
import com.devcci.devtoy.common.exception.ErrorCode;
import com.devcci.devtoy.order.application.dto.OrderResponse;
import com.devcci.devtoy.order.domain.order.Order;
import com.devcci.devtoy.order.domain.order.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderSearchService {
    private final OrderRepository orderRepository;

    public OrderSearchService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse getOrderInfo(Long orderId, String memberId) {
        Order order = orderRepository.findByIdAndMemberId(orderId, memberId)
            .orElseThrow(() -> new ApiException(ErrorCode.ORDER_NOT_FOUND));
        return OrderResponse.of(order.getId(), order.getMemberId(), order.getStatus().name(), order.getStatusReason());
    }
}

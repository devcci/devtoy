package com.devcci.devtoy.order.application.service;

import com.devcci.devtoy.order.domain.order.Order;
import com.devcci.devtoy.order.domain.order.OrderProduct;
import com.devcci.devtoy.order.domain.order.OrderRepository;
import com.devcci.devtoy.order.domain.order.OrderStatus;
import com.devcci.devtoy.order.domain.order.event.OrderCreatedEvent;
import com.devcci.devtoy.order.infra.client.ProductBulkResponse;
import com.devcci.devtoy.order.infra.client.ProductClient;
import com.devcci.devtoy.order.web.dto.OrderRequest;
import com.devcci.devtoy.order.web.dto.OrderRequest.OrderProductRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final ApplicationEventPublisher eventPublisher;
    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderService(
        OrderRepository orderRepository,
        ApplicationEventPublisher eventPublisher,
        ProductClient productClient) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
        this.productClient = productClient;
    }

    @Transactional
    public Long createOrder(String memberId, OrderRequest request) {
        Set<Long> productIds = request.orderProductRequests().stream()
            .map(OrderProductRequest::productId).collect(Collectors.toSet());

        List<ProductBulkResponse> products = productClient.getProductsByIds(productIds);
        Map<Long, ProductBulkResponse> productMap = products.stream()
            .collect(Collectors.toMap(ProductBulkResponse::productId, product -> product));

        Order order = Order.createOrder(memberId, OrderStatus.CREATED);

        for (OrderProductRequest productRequest : request.orderProductRequests()) {
            ProductBulkResponse product = productMap.get(productRequest.productId());
            OrderProduct orderProduct = OrderProduct.createOrderProduct(
                order, productRequest.productId(), productRequest.quantity(), product.price());
            order.addOrderProduct(orderProduct);
        }
        orderRepository.save(order);
        eventPublisher.publishEvent(new OrderCreatedEvent(order));
        return order.getId();
    }

    @Transactional
    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        order.complete();
    }

    @Transactional
    public void cancelOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));
        order.cancel(reason);
    }
}

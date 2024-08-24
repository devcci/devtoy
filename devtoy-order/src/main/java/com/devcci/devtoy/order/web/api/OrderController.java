package com.devcci.devtoy.order.web.api;

import com.devcci.devtoy.common.constans.DevtoyHeaders;
import com.devcci.devtoy.order.application.service.OrderService;
import com.devcci.devtoy.order.web.dto.OrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequestMapping("/order")
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestHeader(DevtoyHeaders.MEMBER_ID) String memberId, @Valid @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.createOrder(memberId, orderRequest);
        return ResponseEntity.created(URI.create("/order/" + orderId)).build();
    }
}

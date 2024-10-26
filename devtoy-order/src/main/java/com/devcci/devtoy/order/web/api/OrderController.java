package com.devcci.devtoy.order.web.api;

import com.devcci.devtoy.common.constants.DevtoyHeaders;
import com.devcci.devtoy.order.application.service.OrderService;
import com.devcci.devtoy.order.web.dto.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Tag(name = "주문 API")
@RequestMapping("/order")
@RestController
public class OrderController {

    private final OrderService orderService;
    private final String gatewayUri;

    public OrderController(OrderService orderService, @Value("${gateway.uri}") String gatewayUri) {
        this.orderService = orderService;
        this.gatewayUri = gatewayUri;
    }

    @Operation(summary = "주문 요청")
    @PostMapping
    public ResponseEntity<Long> createOrder(
        @Parameter(hidden = true)
        @RequestHeader(DevtoyHeaders.MEMBER_ID) String memberId,
        @Valid @RequestBody OrderRequest orderRequest) {
        Long orderId = orderService.createOrder(memberId, orderRequest);
        URI location = UriComponentsBuilder
            .fromUriString(gatewayUri)
            .path("/order/{id}")
            .buildAndExpand(orderId)
            .toUri();
        return ResponseEntity.created(location).body(orderId);
    }

}

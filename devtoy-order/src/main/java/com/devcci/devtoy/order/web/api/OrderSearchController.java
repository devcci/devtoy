package com.devcci.devtoy.order.web.api;

import com.devcci.devtoy.common.constants.DevtoyHeaders;
import com.devcci.devtoy.order.application.dto.OrderResponse;
import com.devcci.devtoy.order.application.service.OrderSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문 조회 API")
@RequestMapping("/order")
@RestController
public class OrderSearchController {

    private final OrderSearchService orderSearchService;

    public OrderSearchController(OrderSearchService orderSearchService) {
        this.orderSearchService = orderSearchService;
    }

    @Operation(summary = "주문 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
        @Parameter(hidden = true) @RequestHeader(DevtoyHeaders.MEMBER_ID) String memberId, @PathVariable Long orderId) {
        OrderResponse orderResponse = orderSearchService.getOrderInfo(orderId, memberId);
        return ResponseEntity.ok(orderResponse);
    }
}

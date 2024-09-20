package com.devcci.devtoy.order.domain.order;

import com.devcci.devtoy.common.domain.BaseTimeEntity;
import com.devcci.devtoy.common.domain.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "status_reason")
    private String statusReason;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 15)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, orphanRemoval = true)
    private final List<OrderProduct> orderProducts = new ArrayList<>();

    private Order(String memberId, OrderStatus status) {
        this.memberId = memberId;
        this.status = status;
        this.totalPrice = BigDecimal.ZERO;
    }

    public static Order createOrder(String memberId, OrderStatus status) {
        return new Order(memberId, status);
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel(String reason) {
        this.status = OrderStatus.CANCELED;
        this.statusReason = reason;
    }

    public List<OrderProduct> getOrderProducts() {
        return Collections.unmodifiableList(orderProducts);
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        this.totalPrice = this.orderProducts.stream()
            .map(op -> op.getPrice().multiply(BigDecimal.valueOf(op.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}

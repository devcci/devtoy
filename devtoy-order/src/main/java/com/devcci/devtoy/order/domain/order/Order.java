package com.devcci.devtoy.order.domain.order;

import com.devcci.devtoy.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Order(Long memberId, OrderStatus status) {
        this.memberId = memberId;
        this.status = status;
    }

    public static Order createOrder(Long memberId, OrderStatus status) {
        return new Order(memberId, status);
    }


    public void order() {
        this.status = OrderStatus.ORDERED;
    }

    public void complete() {
        this.status = OrderStatus.COMPLETED;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }

}

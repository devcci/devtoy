package com.devcci.devtoy.order.domain.order;

import com.devcci.devtoy.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "order_product",
    uniqueConstraints = {
        @UniqueConstraint(name = "order_product_uq", columnNames = {"order_id", "product_id"})
    })
public class OrderProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_fk"))
    private Order order;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @NotNull
    @Column(name = "price", nullable = false, precision = 15, scale = 0)
    private BigDecimal price;

    private OrderProduct(Order order, Long productId, Long quantity, BigDecimal price) {
        this.productId = productId;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderProduct createOrderProduct(Order order, Long productId, Long quantity, BigDecimal price) {
        return new OrderProduct(order, productId, quantity, price);
    }

}
package com.devcci.devtoy.order.domain.order;

import com.devcci.devtoy.order.domain.BaseTimeEntity;
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
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "order_product")
public class OrderProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "order_quantity", nullable = false)
    private Long orderQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_fk"))
    private Order order;

    private OrderProduct(Long productId, Long orderQuantity, Order order) {
        this.productId = productId;
        this.orderQuantity = orderQuantity;
        this.order = order;
    }

    public static OrderProduct createOrderProduct(Long productId, Long orderQuantity, Order order) {
        return new OrderProduct(productId, orderQuantity, order);
    }

}
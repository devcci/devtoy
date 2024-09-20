package com.devcci.devtoy.order.infra.persistence;

import com.devcci.devtoy.order.domain.order.Order;
import com.devcci.devtoy.order.domain.order.OrderProduct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

import static java.time.LocalDateTime.now;

public class OrderSaveRepositoryImpl implements OrderSaveRepository {

    private static final int BATCH_SIZE = 30;

    @PersistenceContext
    private EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    public OrderSaveRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public void saveWithBulk(Order order) {
        entityManager.persist(order);

        List<OrderProduct> orderProducts = order.getOrderProducts();
        jdbcTemplate.batchUpdate(
            "INSERT INTO order_product (order_id, product_id, quantity, price, created_at, modified_at) VALUES (?, ?, ?, ?, ?,?)",
            orderProducts,
            BATCH_SIZE,
            (PreparedStatement ps, OrderProduct product) -> {
                ps.setLong(1, order.getId());
                ps.setLong(2, product.getProductId());
                ps.setLong(3, product.getQuantity());
                ps.setBigDecimal(4, product.getPrice());
                ps.setTimestamp(5, Timestamp.valueOf(now()));
                ps.setTimestamp(6, Timestamp.valueOf(now()));
            }
        );

        entityManager.flush();
        entityManager.clear();
    }
}

package com.devcci.devtoy.order.domain.order;

import com.devcci.devtoy.order.infra.persistence.OrderSaveRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderSaveRepository {

    Optional<Order> findByIdAndMemberId(Long id, String memberId);
}

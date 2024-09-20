package com.devcci.devtoy.product.infra.persistence.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

    Optional<OutboxMessage> findByAggregateTypeAndAggregateId(String aggregateType, String aggregateId);

    List<OutboxMessage> findByStatus(OutboxMessageStatus status);
}

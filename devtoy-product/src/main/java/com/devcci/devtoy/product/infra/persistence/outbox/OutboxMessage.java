package com.devcci.devtoy.product.infra.persistence.outbox;

import com.devcci.devtoy.common.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "outbox_message")
public class OutboxMessage extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Column
    private String aggregateType;
    @Column
    private String aggregateId;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String payload;
    @Enumerated(EnumType.STRING)
    @Column
    private OutboxMessageStatus status = OutboxMessageStatus.PENDING;
    @Column
    private LocalDateTime lastAttemptAt;
    @Column
    private int attempts;


    private OutboxMessage(String aggregateType, String aggregateId, String payload) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.payload = payload;
    }

    public static OutboxMessage createOutboxMessage(String aggregateType, String aggregateId, String payload) {
        return new OutboxMessage(aggregateType, aggregateId, payload);
    }

    public void incrementAttempts() {
        this.attempts++;
        this.lastAttemptAt = LocalDateTime.now();
    }

    public void markAsSent() {
        this.status = OutboxMessageStatus.SENT;
    }
}

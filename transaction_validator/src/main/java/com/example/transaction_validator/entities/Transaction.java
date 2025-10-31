package com.example.transaction_validator.entities;

import java.time.LocalDateTime;

import com.example.transaction_validator.entities.enums.TransactionStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Double amount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_account_id", nullable = false)
    private Account receiverAccount;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private TransactionStatus status = TransactionStatus.INCOMPLETE; // e.g. "APPROVED" or "FLAGGED"

    @PrePersist
    public void generateEnvelopeId() {
        if (this.transactionId == null) {
            this.transactionId = (long)(Math.random() * 90000000L) + 10000000L;
        }
    }
}

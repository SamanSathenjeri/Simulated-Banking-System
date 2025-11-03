package com.example.transaction_validator.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Double balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean active = true;

    @PrePersist
    public void generateAccountId() {
        if (this.accountId == null) {
            this.accountId = (long)(Math.random() * 90000000L) + 10000000L;
        }
    }
}

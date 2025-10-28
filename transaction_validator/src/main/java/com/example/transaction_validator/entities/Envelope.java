package com.example.transaction_validator.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.example.transaction_validator.entities.enums.EnvelopeStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Envelope {
    @Id
    @Column(name = "transactionId")
    private Long envelopeId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "transactionId")
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EnvelopeStatus status = EnvelopeStatus.PENDING; // "COMPLETED" or "PENDING"

    private LocalDateTime signedAt;

    @OneToMany(mappedBy = "envelope", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Signer> signers;
}

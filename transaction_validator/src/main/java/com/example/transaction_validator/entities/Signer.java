package com.example.transaction_validator.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"envelopeId", "userId"})
    }
)
public class Signer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signerId;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "envelopeId", nullable = false)
    private Envelope envelope;

    private String signerEmail;
    private String signedHash; // either the hash or NULL
    private LocalDateTime signedAt; // either the date or NULL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
}

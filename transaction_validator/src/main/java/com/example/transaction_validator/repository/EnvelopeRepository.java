package com.example.transaction_validator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.transaction_validator.entities.Envelope;

public interface EnvelopeRepository extends JpaRepository<Envelope, Long> {
    // Optional: custom queries here later
}
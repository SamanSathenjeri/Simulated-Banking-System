package com.example.transaction_validator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_validator.entities.Envelope;
import com.example.transaction_validator.entities.Signer;
import com.example.transaction_validator.entities.User;

public interface SignerRepository extends JpaRepository<Signer, Long> {
    // Optional: custom queries here later
    public Optional<Signer> findByUserAndEnvelope(User user, Envelope envelope);
    public Optional<List<Signer>> findSignerByEnvelope(Envelope envelope);
    public Optional<List<Signer>> findSignerByUser(User user);
}
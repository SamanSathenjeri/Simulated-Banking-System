package com.example.transaction_validator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.transaction_validator.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Optional: custom queries here later
    Optional<User> findByEmail(String email);
}
package com.example.transaction_validator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.transaction_validator.entities.Account;
import com.example.transaction_validator.entities.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // Optional: custom queries here later
    Optional<List<Account>> findByUser(User user);
}
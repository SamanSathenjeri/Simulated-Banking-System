package com.example.transaction_validator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.transaction_validator.entities.Account;
import com.example.transaction_validator.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Optional: custom queries here later
    Optional<List<Transaction>> findBySenderAccount(Account account);
    Optional<List<Transaction>> findByReceiverAccount(Account account);
}

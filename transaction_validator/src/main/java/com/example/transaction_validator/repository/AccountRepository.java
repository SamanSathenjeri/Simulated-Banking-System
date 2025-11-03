package com.example.transaction_validator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.transaction_validator.entities.Account;
import com.example.transaction_validator.entities.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM Account a WHERE a.user = :user AND a.active = true")
    Optional<List<Account>> findByUserAndActiveTrue(@Param("user") User user);

    @Query("SELECT a FROM Account a WHERE a.active = true")
    List<Account> findAllActive();
}
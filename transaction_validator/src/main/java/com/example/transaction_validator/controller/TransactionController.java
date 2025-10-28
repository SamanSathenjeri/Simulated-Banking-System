package com.example.transaction_validator.controller;

import com.example.transaction_validator.dto.TransactionDTO;
import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.entities.enums.UserRole;
import com.example.transaction_validator.service.AccountService;
import com.example.transaction_validator.service.TransactionService;
import com.example.transaction_validator.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*") // allows Angular to call this API
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService, AccountService accountService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping("/allTransactions")
    public ResponseEntity<?> getAllTransactions() {
        if (userService.getUser().getUserRole().equals(UserRole.ADMIN)){
            return ResponseEntity.ok(transactionService.getAllTransactions());
        }
        else{
            return new ResponseEntity<>("Access Denied: You do not have the required permissions.", HttpStatus.FORBIDDEN);
        }
    }
    
    // Get single transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTransaction(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        Long sender = accountService.getAccountById(transaction.getSenderAccountId()).getUser().getUserId();
        Long receiver =  accountService.getAccountById(transaction.getReceiverAccountId()).getUser().getUserId();
        User user = userService.getUser();
        if (user.getUserId() == sender || user.getUserId() == receiver || user.getUserRole() == UserRole.ADMIN){
            return ResponseEntity.ok(transaction);
        }
        else{
            return new ResponseEntity<>("Access Denied: You do not have the required permissions.", HttpStatus.FORBIDDEN);
        }
    }
}

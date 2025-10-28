package com.example.transaction_validator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.transaction_validator.dto.TransactionDTO;
import com.example.transaction_validator.entities.Account;
import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.entities.enums.UserRole;
import com.example.transaction_validator.service.AccountService;
import com.example.transaction_validator.service.TransactionService;
import com.example.transaction_validator.service.UserService;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "*") // allows Angular to call this API
public class AccountController {
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(UserService userService, AccountService accountService, TransactionService transactionService) {
        this.userService = userService;
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping("/newaccount")
    public ResponseEntity<?> createNewAccount(){
        User user = userService.getUser();
        Account account = new Account();
        account.setBalance(0.0);
        account.setUser(user);
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        if (userService.getUser().getUserRole().equals(UserRole.ADMIN)){
            return ResponseEntity.ok(accountService.getAllAccounts());
        }
        else{
            return new ResponseEntity<>("Access Denied: You do not have the required permissions.", HttpStatus.FORBIDDEN);
        }
    }
    
    // Get single account by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountDTOById(id));
    }

    @DeleteMapping("/{id}/deleteaccount")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) { 
        return ResponseEntity.ok(accountService.deleteAccount(id));
    }

    @GetMapping("/{id}/senttransactions")
    public ResponseEntity<?> getSentTransactionsByAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getSentTransactionsByAccount(id));
    }

    @GetMapping("/{id}/receivedtransactions")
    public ResponseEntity<?> getReceivedTransactionsByAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getReceivedTransactionsByAccount(id));
    }

    @PostMapping("/{id}/newtransaction")
    public ResponseEntity<?> createTransaction(@RequestParam Double amount,
                                                @PathVariable Long id,
                                                @RequestParam Long receiverAccount) {
        TransactionDTO transaction = new TransactionDTO(null, amount, null, id, receiverAccount, null);
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @GetMapping("/{id}/getaccountbalance")
    public ResponseEntity<?> getAccountBalance(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getBalance(id));
    }

    @PostMapping("/{id}/addbalance")
    public ResponseEntity<?> addBalance(@PathVariable Long id, @RequestParam Double amount){
        if(amount >= 0){
            accountService.addToBalance(accountService.getAccountById(id), amount);
            return ResponseEntity.ok(true);
        }
        else{
            return ResponseEntity
                .badRequest()
                .body("Invalid amount added to balance");
        }
    } 
}
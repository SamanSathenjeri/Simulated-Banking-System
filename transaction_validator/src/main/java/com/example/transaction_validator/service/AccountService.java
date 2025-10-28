package com.example.transaction_validator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_validator.dto.AccountDTO;
import com.example.transaction_validator.dto.TransactionDTO;
import com.example.transaction_validator.entities.Account;
import com.example.transaction_validator.entities.Transaction;
import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.repository.AccountRepository;
import com.example.transaction_validator.repository.TransactionRepository;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Autowired
    public AccountService(AccountRepository accountRepository, 
            TransactionRepository transactionRepository,
            UserService userService){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    @Transactional
    public AccountDTO createAccount(Account account){
        return mapToDTO(accountRepository.save(account));
    }

    public AccountDTO getAccountDTOById(Long accountId){
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        return mapToDTO(account);
    }

    public Account getAccountById(Long accountId){
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
    }

    public Double getBalance(Long accountId){
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        return account.getBalance();
    }

    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Transactional
    public boolean deleteAccount(Long accountId) {
        accountRepository.delete(getAccountById(accountId));
        return true;
    }

    public List<AccountDTO> getAccountsByUser(){
        User user = userService.getUser();
        List<Account> accounts = accountRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("No accounts found with User: " + user.getUserId()));
        return accounts.stream().map(this::mapToDTO).toList();
    }

    public List<TransactionDTO> getSentTransactionsByAccount(Long accountId){
        Account account = getAccountById(accountId);
        List<Transaction> transactions = transactionRepository.findBySenderAccount(account)
            .orElseThrow(() -> new RuntimeException("No transactions found with Account: " + accountId));
        return transactions.stream().map(transaction -> new TransactionDTO(
                        transaction.getTransactionId(), 
                        transaction.getAmount(),
                        transaction.getTimestamp(),
                        transaction.getSenderAccount().getAccountId(),
                        transaction.getReceiverAccount().getAccountId(),
                        transaction.getStatus()
                    ))
                    .toList();
    }

    public List<TransactionDTO> getReceivedTransactionsByAccount(Long accountId){
        Account account = getAccountById(accountId);
        List<Transaction> transactions = transactionRepository.findByReceiverAccount(account)
            .orElseThrow(() -> new RuntimeException("No transactions found with Account: " + accountId));
        return transactions.stream().map(transaction -> new TransactionDTO(
                        transaction.getTransactionId(), 
                        transaction.getAmount(),
                        transaction.getTimestamp(),
                        transaction.getSenderAccount().getAccountId(),
                        transaction.getReceiverAccount().getAccountId(),
                        transaction.getStatus()
                    ))
                    .toList();
    }

    public void addToBalance(Account account, Double amount ){
        account.setBalance(account.getBalance()+amount);
        accountRepository.save(account);
    }
    
    public AccountDTO mapToDTO(Account account){
        return new AccountDTO(account.getAccountId(), account.getBalance(), account.getUser().getUserId());
    }
}

package com.example.transaction_validator.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_validator.dto.TransactionDTO;
import com.example.transaction_validator.entities.Account;
import com.example.transaction_validator.entities.Transaction;
import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.repository.TransactionRepository;

import com.example.transaction_validator.entities.enums.*;
import com.example.transaction_validator.event.EnvelopeCompletedEvent;
import com.example.transaction_validator.event.TransactionCreatedEvent;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ApplicationEventPublisher eventPublisher, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.eventPublisher = eventPublisher;
        this.accountService = accountService;
    }

    @Transactional
    public boolean createTransaction(TransactionDTO transactionDTO) {
        Account senderAccount = accountService.getAccountById(transactionDTO.getSenderAccountId());
        User sender = senderAccount.getUser();
        Account receiverAccount = accountService.getAccountById(transactionDTO.getReceiverAccountId());
        User receiver = receiverAccount.getUser();
        Transaction transaction = new Transaction(transactionDTO.getTransactionID(), 
            transactionDTO.getAmount(), transactionDTO.getTimestamp(),
            sender, senderAccount, receiver, receiverAccount, transactionDTO.getStatus());
        transaction.setTimestamp(LocalDateTime.now());
        System.out.println(transaction.getStatus());
        if (transaction.getStatus() == TransactionStatus.INCOMPLETE || !checkValidTransaction(transaction)){
            return false;
        }

        if (transaction.getAmount() > 10000) {
            transaction.setStatus(TransactionStatus.FLAGGED);
            transaction = transactionRepository.save(transaction);
            eventPublisher.publishEvent(new TransactionCreatedEvent(this, transaction));
        } 
        else {
            transaction.setStatus(TransactionStatus.APPROVED);
            completeTransaction(transaction);
            transaction = transactionRepository.save(transaction);
        }
        return true;
    }

    @Transactional
    @EventListener
    public void handleEnvelopeCompleted(EnvelopeCompletedEvent event) {
        Transaction transaction = event.getTransaction();
        transaction.setStatus(TransactionStatus.APPROVED);
        completeTransaction(transaction);
        transactionRepository.save(transaction);
    }

    public TransactionDTO getTransactionById(Long transactionId){
        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));
        return mapToDTO(transaction);
    }

    public List<TransactionDTO> getAllTransactions(){
        return transactionRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    public boolean checkValidTransaction(Transaction transaction){
        if(transaction.getSender().getUserId() == null 
           || transaction.getSenderAccount().getAccountId() == null
           || transaction.getReceiver().getUserId() == null
           || transaction.getReceiverAccount().getAccountId() == null
           || transaction.getSenderAccount().getBalance() - transaction.getAmount() < 0.0) {
            return false;
        }
        return true;
    }

    public void completeTransaction(Transaction transaction){
        accountService.addToBalance(transaction.getSenderAccount(), -(transaction.getAmount()));
        accountService.addToBalance(transaction.getReceiverAccount(), transaction.getAmount());
        transaction = transactionRepository.save(transaction);
    }

    public TransactionDTO mapToDTO(Transaction transaction){
        return new TransactionDTO(transaction.getTransactionId(), transaction.getAmount(), transaction.getTimestamp(), 
                                    transaction.getSenderAccount().getAccountId(), transaction.getReceiverAccount().getAccountId(),
                                    transaction.getStatus());
    }
}

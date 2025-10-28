package com.example.transaction_validator.event;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.example.transaction_validator.entities.Transaction;
import com.example.transaction_validator.entities.enums.TransactionStatus;
import com.example.transaction_validator.service.TransactionService;

public class EnvelopeCompletedEvent {
    private final Long envelopeId;
    private final Transaction transaction;
    private final TransactionService transactionService;

    public EnvelopeCompletedEvent(Long envelopeId, Transaction transaction, TransactionService transactionService) {
        this.envelopeId = envelopeId;
        this.transaction = transaction;
        this.transactionService = transactionService;
    }

    public Long getEnvelopeId() { return envelopeId; }
    public Transaction getTransaction() { return transaction; }
    
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEnvelopeCompletedEvent(EnvelopeCompletedEvent event) {
        Transaction transaction = event.getTransaction();
        transaction.setStatus(TransactionStatus.APPROVED);
        transactionService.completeTransaction(transaction);
    }
}
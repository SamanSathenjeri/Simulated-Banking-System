package com.example.transaction_validator.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_validator.dto.EnvelopeDTO;
import com.example.transaction_validator.dto.SignerDTO;
import com.example.transaction_validator.entities.Envelope;
import com.example.transaction_validator.entities.Signer;
import com.example.transaction_validator.entities.Transaction;
import com.example.transaction_validator.entities.enums.EnvelopeStatus;
import com.example.transaction_validator.event.EnvelopeCompletedEvent;
import com.example.transaction_validator.event.TransactionCreatedEvent;
import com.example.transaction_validator.repository.EnvelopeRepository;
import com.example.transaction_validator.repository.SignerRepository;

@Service
public class EnvelopeService {

    private final EnvelopeRepository envelopeRepository;
    private final SignerService signerService;
    private final ApplicationEventPublisher eventPublisher;
    private final SignerRepository signerRepository;
    private final TransactionService transactionService;

    @Autowired
    public EnvelopeService(EnvelopeRepository envelopeRepository, SignerService signerService, ApplicationEventPublisher eventPublisher, SignerRepository signerRepository, TransactionService transactionService){
        this.envelopeRepository = envelopeRepository;
        this.signerService = signerService;
        this.eventPublisher = eventPublisher;
        this.signerRepository = signerRepository;
        this.transactionService = transactionService;
    }

    @EventListener
    @Transactional
    public void handleTransactionCreated(TransactionCreatedEvent event) {
        Transaction transaction = event.getTransaction();
        System.out.println("Received event: creating envelope for transaction " + transaction.getTransactionId());
        createEnvelopeForTransaction(transaction);
    }

    // @Transactional
    public EnvelopeDTO createEnvelopeForTransaction(Transaction transaction) {
        Envelope envelope = new Envelope();
        envelope.setTransaction(transaction);
        envelope = envelopeRepository.save(envelope);
        List<Signer> signers = signerService.createSigners(transaction, envelope);
        for (Signer signer : signers) {
            signerRepository.save(signer);
        }

        envelope.setSigners(signers);
        return mapToDTO(envelopeRepository.save(envelope));
    }

    @Transactional
    public void markTransactionComplete(Long envelopeId) {
        Envelope env = envelopeRepository.findById(envelopeId)
                .orElseThrow(() -> new RuntimeException("Envelope not found"));
        env.setStatus(EnvelopeStatus.COMPLETED);
        env.setSignedAt(LocalDateTime.now());
        envelopeRepository.save(env);

        eventPublisher.publishEvent(new EnvelopeCompletedEvent(envelopeId, env.getTransaction(), transactionService));
    }

    @Transactional
    public boolean checkEnvelopeStatus(Long envelopeId){
        Envelope envelope = envelopeRepository.findById(envelopeId)
            .orElseThrow(() -> new RuntimeException("Envelope not found"));

        for (Signer signer: envelope.getSigners()){
            if (signer.getSignedAt() == null || signer.getSignedHash() == null){
                return false;
            }
        }
        if (envelope.getSignedAt() == null){
            markTransactionComplete(envelopeId);
        }
        return true;
    }

    public Envelope getEnvelopeById(Long envelopeId){
        Envelope envelope = envelopeRepository.findById(envelopeId)
            .orElseThrow(() -> new RuntimeException("Envelope not found"));
        return envelope;
    }

    public EnvelopeDTO getEnvelopeDTOById(Long envelopeId){
        Envelope envelope = envelopeRepository.findById(envelopeId)
            .orElseThrow(() -> new RuntimeException("Envelope not found"));
        return mapToDTO(envelope);
    }

    public List<SignerDTO> getSignersForEnvelope(Long envelopeId){
        Envelope envelope = envelopeRepository.findById(envelopeId)
            .orElseThrow(() -> new RuntimeException("Envelope not found"));
        return signerService.getSignersForEnvelope(envelope);
    }

    public List<EnvelopeDTO> getAllEnvelopes(){
        return envelopeRepository.findAll().stream().map(this::mapToDTO).toList();
    }
    
    public EnvelopeDTO mapToDTO(Envelope envelope){
        return new EnvelopeDTO(envelope.getEnvelopeId(), envelope.getTransaction().getTransactionId(),
                    envelope.getStatus(), envelope.getSignedAt());
    }
}


package com.example.transaction_validator.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.transaction_validator.dto.SignerDTO;
import com.example.transaction_validator.entities.Envelope;
import com.example.transaction_validator.entities.Signer;
import com.example.transaction_validator.entities.Transaction;
import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.repository.SignerRepository;
import com.example.transaction_validator.security.Sha256Util;

@Service
public class SignerService {
    private final SignerRepository signerRepository;

    @Autowired
    public SignerService(SignerRepository signerRepository){
        this.signerRepository = signerRepository;
    }

    @Transactional
    public List<Signer> createSigners(Transaction transaction, Envelope envelope){
        List<Signer> signers = new ArrayList<>();
        Signer receiverSigner = new Signer();
        receiverSigner.setEnvelope(envelope);
        receiverSigner.setSignerEmail(transaction.getReceiver().getEmail());
        receiverSigner.setUser(transaction.getReceiver());
        signers.add(receiverSigner);

        if (transaction.getReceiver().getUserId() != transaction.getSender().getUserId()){
            Signer senderSigner = new Signer();
            senderSigner.setEnvelope(envelope);
            senderSigner.setSignerEmail(transaction.getSender().getEmail());
            senderSigner.setUser(transaction.getSender());
            signers.add(senderSigner);
        }
        
        return signers;
    }

    public List<SignerDTO> getSignersForEnvelope(Envelope envelope){
        List<Signer> signers = signerRepository.findSignerByEnvelope(envelope)
            .orElse(Collections.emptyList());
        return signers.stream()
            .map(this::mapToDTO)
            .toList();
    }

    @Transactional
    public boolean signEnvelopeWithEnvelope(Envelope envelope, User signerUser){
        Signer signer = signerRepository.findByUserAndEnvelope(signerUser, envelope)
                .orElseThrow(() -> new RuntimeException("Signer not found"));
        signer.setSignedAt(LocalDateTime.now());
        signer.setSignedHash(Sha256Util.generate(signerUser.getEmail() + LocalDateTime.now()));
        signerRepository.save(signer);
        return true;
    }

    public List<SignerDTO> getSignersForUser(User user){
        List<Signer> signers = signerRepository.findSignerByUser(user)
            .orElse(Collections.emptyList());
        return signers.stream()
            .map(this::mapToDTO)
            .toList();
    }

    public SignerDTO mapToDTO(Signer signer){
        return new SignerDTO(signer.getSignerId(), signer.getEnvelope().getEnvelopeId(), 
                    signer.getSignerEmail(), signer.getSignedAt(), signer.getUser().getUserId());
    }
}

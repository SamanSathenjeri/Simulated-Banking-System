package com.example.transaction_validator.controller;

import com.example.transaction_validator.entities.Envelope;
import com.example.transaction_validator.entities.User;
import com.example.transaction_validator.entities.enums.UserRole;
import com.example.transaction_validator.service.EnvelopeService;
import com.example.transaction_validator.service.SignerService;
import com.example.transaction_validator.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/envelopes")
@CrossOrigin(origins = "*")
public class EnvelopeController {

    private final EnvelopeService envelopeService;
    private final UserService userService;
    private final SignerService signerService;

    @Autowired
    public EnvelopeController(EnvelopeService envelopeService, UserService userService, SignerService signerService) {
        this.envelopeService = envelopeService;
        this.userService = userService;
        this.signerService = signerService;
    }

    // Get all envelopes
    @GetMapping
    public ResponseEntity<?> getAllEnvelopes() {
        User user = userService.getUser();
        if (user.getUserRole().equals(UserRole.ADMIN)){
            return ResponseEntity.ok(envelopeService.getAllEnvelopes());
        }
        else{
            return new ResponseEntity<>("Access Denied: You do not have the required permissions.", HttpStatus.FORBIDDEN);
        }
    }

    // Get envelope by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getEnvelope(@PathVariable Long id) {
        return ResponseEntity.ok(envelopeService.getEnvelopeDTOById(id));
    }

    // Mark envelope as manually completed (admin use)
    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completeEnvelope(@PathVariable Long id) {
        User user = userService.getUser();
        if (user.getUserRole().equals(UserRole.ADMIN)){
            envelopeService.markTransactionComplete(id);
            return ResponseEntity.ok("Envelope marked as complete");
        }
        else{
            return new ResponseEntity<>("Access Denied: You do not have the required permissions.", HttpStatus.FORBIDDEN);
        }
    }

    // Endpoint to sign an envelope
    @PostMapping("/{id}/sign")
    public ResponseEntity<?> signEnvelope(@PathVariable Long id) {
        User signer = userService.getUser();
        Envelope envelope = envelopeService.getEnvelopeById(id);
        return ResponseEntity.ok(signerService.signEnvelopeWithEnvelope(envelope, signer));
    }

    @GetMapping("/{id}/checkstatus")
    public ResponseEntity<?> checkEnvelopeStatus(@PathVariable Long id) {
        return ResponseEntity.ok(envelopeService.checkEnvelopeStatus(id));
    }

    // @GetMapping("/envelopesbyuser")
    // public ResponseEntity<?> getEnvelopesByUser() {
    //     User user = userService.getUser();
    //     return ResponseEntity.ok(envelopeService.);
    // }
}

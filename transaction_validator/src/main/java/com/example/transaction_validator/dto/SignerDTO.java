package com.example.transaction_validator.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignerDTO {
    private Long signerId;
    private Long envelopeId;
    private String signerEmail;
    private LocalDateTime signedAt;
    private Long userId;
}

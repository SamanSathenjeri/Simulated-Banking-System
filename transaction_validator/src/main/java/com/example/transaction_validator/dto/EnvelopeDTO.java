package com.example.transaction_validator.dto;

import java.time.LocalDateTime;
import com.example.transaction_validator.entities.enums.EnvelopeStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvelopeDTO {
    private Long envelopeId;
    private Long transactionId;
    private EnvelopeStatus status = EnvelopeStatus.PENDING;
    private LocalDateTime signedAt;
}
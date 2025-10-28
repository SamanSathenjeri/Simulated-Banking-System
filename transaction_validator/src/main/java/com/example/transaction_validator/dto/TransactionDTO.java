package com.example.transaction_validator.dto;

import java.time.LocalDateTime;
import com.example.transaction_validator.entities.enums.TransactionStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long transactionID;
    private Double amount;
    private LocalDateTime timestamp;
    private Long senderAccountId;
    private Long receiverAccountId;
    private TransactionStatus status;
}

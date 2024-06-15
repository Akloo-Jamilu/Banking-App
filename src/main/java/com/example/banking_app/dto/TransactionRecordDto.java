package com.example.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRecordDto {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}

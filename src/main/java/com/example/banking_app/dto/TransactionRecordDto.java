package com.example.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRecordDto {
    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}

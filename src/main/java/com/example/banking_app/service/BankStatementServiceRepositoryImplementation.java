package com.example.banking_app.service;

import com.example.banking_app.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BankStatementServiceRepositoryImplementation {
    private TransactionRepository transactionRepository
}

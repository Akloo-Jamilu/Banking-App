package com.example.banking_app.service;

import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class BankStatementServiceRepositoryImplementation {
    private TransactionRepository transactionRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate ){
        List<Transaction> transactions = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber));
    }
}

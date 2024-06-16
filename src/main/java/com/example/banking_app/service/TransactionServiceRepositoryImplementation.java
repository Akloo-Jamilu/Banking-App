package com.example.banking_app.service;

import com.example.banking_app.dto.TransactionRecordDto;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepository;
import com.example.banking_app.service.servicesRepository.TransactionServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceRepositoryImplementation implements TransactionServiceRepository {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceRepositoryImplementation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public void saveTransaction(TransactionRecordDto transactionRecordDto) {
        try {
            Transaction transaction = Transaction.builder()
                    .transactionType(transactionRecordDto.getTransactionType())
                    .accountNumber(transactionRecordDto.getAccountNumber())
                    .amount(transactionRecordDto.getAmount())
                    .status("Success")
                    .build();

            transactionRepository.save(transaction);
            System.out.println("Transaction saved successfully");
        } catch (Exception e) {
            // Handle exceptions appropriately, maybe log the error or rethrow it
            System.err.println("Failed to save transaction: " + e.getMessage());
        }
    }
}

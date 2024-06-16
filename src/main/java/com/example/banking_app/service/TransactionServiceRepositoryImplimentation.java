package com.example.banking_app.service;

import com.example.banking_app.dto.TransactionRecordDto;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepository;
import com.example.banking_app.service.servicesRepository.TransactionServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionServiceRepositoryImplimentation implements TransactionServiceRepository {

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionRecordDto transactionRecordDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionRecordDto.getTransactionType())
                .accountNumber(transactionRecordDto.getAccountNumber())
                .amount(transactionRecordDto.getAmount())
                .build();


    }
}

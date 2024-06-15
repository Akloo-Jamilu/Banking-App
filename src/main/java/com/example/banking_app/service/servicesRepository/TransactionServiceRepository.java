package com.example.banking_app.service.servicesRepository;


import com.example.banking_app.dto.TransactionRecordDto;

public interface TransactionServiceRepository {
    void saveTransaction(TransactionRecordDto transactionRecordDto);
}

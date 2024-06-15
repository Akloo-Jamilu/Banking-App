package com.example.banking_app.service.servicesRepository;

import com.example.banking_app.entity.Transaction;

public interface TransactionServiceRepository {
    void saveTransaction(Transaction transaction);
}

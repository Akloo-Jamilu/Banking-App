package com.example.banking_app;

import com.example.banking_app.dto.TransactionRecordDto;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepository;
import com.example.banking_app.service.TransactionServiceRepositoryImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

public class TransactionServiceRepositoryImplementationTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceRepositoryImplementation transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTransaction() {
        TransactionRecordDto transactionRecordDto = new TransactionRecordDto();
        transactionRecordDto.setTransactionType("debit");
        transactionRecordDto.setAccountNumber("123456");
        transactionRecordDto.setAmount(new BigDecimal("100.00"));

        transactionService.saveTransaction(transactionRecordDto);

        verify(transactionRepository).save(any(Transaction.class));
    }
}

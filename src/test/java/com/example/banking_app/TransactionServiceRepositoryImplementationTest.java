package com.example.banking_app;


import com.example.banking_app.dto.TransactionRecordDto;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepository;
import com.example.banking_app.repository.USerRepository;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.service.TransactionServiceRepositoryImplementation;
import com.example.banking_app.service.UserServiceRepositoryImplimentation;
import com.example.banking_app.utils.AccountUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.banking_app.dto.TransferDto;
import com.example.banking_app.entity.User;

import com.example.banking_app.service.servicesRepository.EmailServiceRepository;
import com.example.banking_app.service.servicesRepository.TransactionServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


import java.math.BigDecimal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

public class TransactionServiceRepositoryImplementationTest {

    @Mock
    private USerRepository userRepository;

    @Mock
    private EmailServiceRepository emailServiceRepository;

    @Mock
    private TransactionServiceRepository transactionServiceRepository;

    @InjectMocks
    private UserServiceRepositoryImplimentation userService;

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

    @Test
    public void testTransfer() {
        TransferDto transferDto = new TransferDto();
        transferDto.setSourceAccount("123");
        transferDto.setDestinationAccount("456");
        transferDto.setAmount(new BigDecimal("50"));

        User sourceUser = new User();
        sourceUser.setAccountNumber("123");
        sourceUser.setAccountBalance(new BigDecimal("100"));

        User destinationUser = new User();
        destinationUser.setAccountNumber("456");
        destinationUser.setAccountBalance(new BigDecimal("100"));

        when(userRepository.existsByAccountNumber("123")).thenReturn(true);
        when(userRepository.existsByAccountNumber("456")).thenReturn(true);
        when(userRepository.findByAccountNumber("123")).thenReturn(sourceUser);
        when(userRepository.findByAccountNumber("456")).thenReturn(destinationUser);

        ResponseEntity<BankRespons> response = userService.transfer(transferDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(AccountUtilities.TRANSFER_SUCCESSFUL_CODE, response.getBody().getResponseCode());
    }
}

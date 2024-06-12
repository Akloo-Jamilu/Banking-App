package com.example.banking_app.service.servicesRepository;

import com.example.banking_app.dto.EnquiryDto;
import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.dto.UserDto;
import com.example.banking_app.respons.BankRespons;
import org.springframework.http.ResponseEntity;

public interface UserServiceRepository {
    BankRespons createAccount(UserDto userDto);
    ResponseEntity<BankRespons> balanceEnquiry(EnquiryDto enquiryDto);
    ResponseEntity<BankRespons> nameEnquiry(EnquiryDto enquiryDto);
    ResponseEntity<BankRespons> creditAccount(TransactionDto transactionDto);
    ResponseEntity<BankRespons> debitACCount(TransactionDto transactionDto);
}

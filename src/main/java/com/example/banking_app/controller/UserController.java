package com.example.banking_app.controller;

import com.example.banking_app.dto.EnquiryDto;
import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.dto.TransferDto;
import com.example.banking_app.dto.UserDto;
import com.example.banking_app.entity.Transaction;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.service.BankStatementServiceRepositoryImplementation;
import com.example.banking_app.service.servicesRepository.UserServiceRepository;
import com.itextpdf.text.DocumentException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Services APIs")
public class UserController {


    private BankStatementServiceRepositoryImplementation bankStatementServiceRepositoryImplementation;

    @Autowired
    public void BankStatementController(BankStatementServiceRepositoryImplementation bankStatementServiceRepositoryImplementation) {
        this.bankStatementServiceRepositoryImplementation = bankStatementServiceRepositoryImplementation;
    }
    @Autowired
    UserServiceRepository userServiceRepository;

    @Autowired
    public UserController(BankStatementServiceRepositoryImplementation bankStatementServiceRepositoryImplementation, UserServiceRepository userServiceRepository) {
        this.bankStatementServiceRepositoryImplementation = bankStatementServiceRepositoryImplementation;
        this.userServiceRepository = userServiceRepository;
    }

//    create customer end point
    @Operation(
            summary = "Creating a new customer to the application"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping
    public BankRespons createAccount(@RequestBody UserDto userDto){
        return userServiceRepository.createAccount(userDto);
    }

//    get user account balance
    @GetMapping("balanceEnquiry")
    public ResponseEntity<BankRespons> balanceEnquiry(@RequestBody EnquiryDto enquiryDto){
        return userServiceRepository.balanceEnquiry(enquiryDto);
    }

//    get user account name
    @GetMapping("nameEnquiry")
    public ResponseEntity<BankRespons> nameEnquiry(@RequestBody EnquiryDto enquiryDto) {
        return userServiceRepository.nameEnquiry(enquiryDto);
    }

//    credit customer account
    @PostMapping("credit")
    public ResponseEntity<BankRespons> creditAccount(@RequestBody TransactionDto transactionDto){
        return userServiceRepository.creditAccount(transactionDto);
    }

    @PostMapping("debit")
    public ResponseEntity<BankRespons> debitAccount(@RequestBody TransactionDto transactionDto){
        return userServiceRepository.debitACCount(transactionDto);
    }

    @PostMapping("transfer")
    public ResponseEntity<BankRespons> transfer(@RequestBody TransferDto transferDto){
        return userServiceRepository.transfer(transferDto);
    }

    @GetMapping("statement")
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
return bankStatementServiceRepositoryImplementation.generateStatement(accountNumber, startDate, endDate);
    }
}

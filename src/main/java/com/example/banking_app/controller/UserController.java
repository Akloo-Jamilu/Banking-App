package com.example.banking_app.controller;

import com.example.banking_app.dto.EnquiryDto;
import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.dto.UserDto;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.service.servicesRepository.UserServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserServiceRepository userServiceRepository;

    @Autowired
    public UserController(UserServiceRepository userServiceRepository) {
        this.userServiceRepository = userServiceRepository;
    }

//    create customer end point
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
}

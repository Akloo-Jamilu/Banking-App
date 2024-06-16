package com.example.banking_app.service;
import com.example.banking_app.dto.*;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.USerRepository;
import com.example.banking_app.respons.AccountInfo;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.service.servicesRepository.EmailServiceRepository;
import com.example.banking_app.service.servicesRepository.TransactionServiceRepository;
import com.example.banking_app.service.servicesRepository.UserServiceRepository;
import com.example.banking_app.utils.AccountUtilities;
import com.example.banking_app.utils.UserValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

@Service
public class UserServiceRepositoryImplimentation implements UserServiceRepository {

    private final USerRepository uSerRepository;
    private final UserValidations userValidations;



    @Autowired
    EmailServiceRepository emailServiceRepository;

    @Autowired
    TransactionServiceRepository transactionServiceRepository;

    @Autowired
    public UserServiceRepositoryImplimentation(USerRepository uSerRepository, Validator validator) {
        this.uSerRepository = uSerRepository;
        this.userValidations = new UserValidations(validator);

    }


//    creating a customer and assigning an account balance
    @Override
    public BankRespons createAccount(UserDto userDto) {
        // Validate UserDto
        userValidations.validateUserDto(userDto);

        // Check if user exists
        if (uSerRepository.existsByEmail(userDto.getEmail())) {
            return BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // Create new User entity
        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .otherNme(userDto.getOtherNme())
                .gender(userDto.getGender())
                .address(userDto.getAddress())
                .stateOfOrigin(userDto.getStateOfOrigin())
                .accountNumber(AccountUtilities.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .alternativePhoneNumber(userDto.getAlternativePhoneNumber())
                .status("Active")
                .build();

        // Save User entity to the repository
        User saveUser = uSerRepository.save(user);
        //email created user
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(saveUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("We are thrilled to have you on board, looking forward to serving you. \n " +
                        "Here is your Account Details: \n" +
                        " Account Name: " + saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherNme() +  " \n" +
                        "Account Number: " + saveUser.getAccountNumber())
                .build();
        emailServiceRepository.sendEmailAlert(emailDetails);
        // Build and return response
        return BankRespons.builder()
                .responseCode(AccountUtilities.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtilities.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(saveUser.getAccountBalance())
                        .accountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherNme())
                        .build())
                .build();
    }



//    getting customer account balance
    @Override
    public ResponseEntity<BankRespons> balanceEnquiry(EnquiryDto enquiryDto) {
        boolean isAccountExist = uSerRepository.existsByAccountNumber(enquiryDto.getAccountNumber());
        if (!isAccountExist){
            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.NOT_FOUND);
        }
        User foundUser = uSerRepository.findByAccountNumber(enquiryDto.getAccountNumber());
        BankRespons bankRespons = BankRespons.builder()
                .responseCode(AccountUtilities.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtilities.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherNme())
                        .build())
                .build();
        return new ResponseEntity<>(bankRespons, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<BankRespons> nameEnquiry(EnquiryDto enquiryDto) {
        boolean isAccountExist = uSerRepository.existsByAccountNumber(enquiryDto.getAccountNumber());
        if (!isAccountExist) {
            BankRespons bankRespons = new BankRespons(AccountUtilities.ACCOUNT_NOT_EXIST_MESSAGE, null);
            return new ResponseEntity<>(bankRespons, HttpStatus.NOT_FOUND);
        }

        User foundUser = uSerRepository.findByAccountNumber(enquiryDto.getAccountNumber());
        String accountName = foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherNme();
//        BankRespons bankRespons = new BankRespons("Account found", accountName);
        BankRespons bankRespons = BankRespons.builder()
                .responseCode(AccountUtilities.ACCOUNT_NAME_FOUND_CODE)
                .responseMessage(AccountUtilities.ACCOUNT_NAME_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(accountName)
                        .build())
                .build();
        return new ResponseEntity<>(bankRespons, HttpStatus.OK);
    }
//    public String nameEnquiry(EnquiryDto enquiryDto) {
//        boolean isAccountExist = uSerRepository.existsByAccountNumber(enquiryDto.getAccountNumber());
//        if (!isAccountExist){
//            return AccountUtilities.ACCOUNT_NOT_EXIST_MESSAGE;
//        }
//        User foundUser = uSerRepository.findByAccountNumber(enquiryDto.getAccountNumber());
//        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherNme();
//
//    }

    @Override
    public ResponseEntity<BankRespons> creditAccount(TransactionDto transactionDto) {
        boolean isAccountExist = uSerRepository.existsByAccountNumber(transactionDto.getAccountNumber());
        if (!isAccountExist){
            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.NOT_FOUND);
        }
        User userToCredit = uSerRepository.findByAccountNumber(transactionDto.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(transactionDto.getAmount()));
        uSerRepository.save(userToCredit);

//        save transaction on transaction table
        TransactionRecordDto transactionRecordDto = TransactionRecordDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("Credit Alert")
                .amount(transactionDto.getAmount())
                .build();
        transactionServiceRepository.saveTransaction(transactionRecordDto);

        BankRespons bankRespons = BankRespons.builder()
                .responseCode(AccountUtilities.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtilities.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherNme())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(userToCredit.getAccountNumber())
                        .build())
                .build();
        return new ResponseEntity<>(bankRespons, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BankRespons> debitACCount(TransactionDto transactionDto) {
        boolean isAccountExist = uSerRepository.existsByAccountNumber(transactionDto.getAccountNumber());
        if (!isAccountExist) {
            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.NOT_FOUND);
        }

        User userToDebit = uSerRepository.findByAccountNumber(transactionDto.getAccountNumber());
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = transactionDto.getAmount().toBigInteger();

        if (availableBalance.compareTo(debitAmount) < 0) {
            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.BAD_REQUEST);
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(transactionDto.getAmount()));
            uSerRepository.save(userToDebit);

            //        save transaction on transaction table
            TransactionRecordDto transactionRecordDto = TransactionRecordDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("Debit Alert")
                    .amount(transactionDto.getAmount())
                    .build();
            transactionServiceRepository.saveTransaction(transactionRecordDto);

            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_DEBITED_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(transactionDto.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherNme())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.OK);
        }
    }

    @Override
    @Transactional // Ensures atomicity
    public ResponseEntity<BankRespons> transfer(TransferDto transferDto) {
        // Check if destination account exists
        boolean isDestinationAccountExist = uSerRepository.existsByAccountNumber(transferDto.getDestinationAccount());
        if (!isDestinationAccountExist) {
            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage("Destination account number " + transferDto.getDestinationAccount() + " does not exist.")
                    .accountInfo(null)
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.NOT_FOUND);
        }

        // Check if source account exists
        boolean isSourceAccountExist = uSerRepository.existsByAccountNumber(transferDto.getSourceAccount());
        if (!isSourceAccountExist) {
            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage("Source account number " + transferDto.getSourceAccount() + " does not exist.")
                    .accountInfo(null)
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.NOT_FOUND);
        }

        User sourceAccount = uSerRepository.findByAccountNumber(transferDto.getSourceAccount());
        BigDecimal transferAmount = transferDto.getAmount();

        // Check if the source account has sufficient balance
        if (transferAmount.compareTo(sourceAccount.getAccountBalance()) > 0) {
            BankRespons bankRespons = BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
            return new ResponseEntity<>(bankRespons, HttpStatus.BAD_REQUEST);
        }

        // Perform the debit and credit operations atomically
        try {
            // Debit the source account
            sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferAmount));
            uSerRepository.save(sourceAccount);
            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("Debit Notification")
                    .recipient(sourceAccount.getEmail())
                    .messageBody("The sum of " + transferAmount + " has been deducted from your account. Your current balance is " + sourceAccount.getAccountBalance())
                    .build();
            emailServiceRepository.sendEmailAlert(debitAlert);

            // Credit the destination account
            User destinationAccountUser = uSerRepository.findByAccountNumber(transferDto.getDestinationAccount());
            destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(transferAmount));
            uSerRepository.save(destinationAccountUser);
            EmailDetails creditAlert = EmailDetails.builder()
                    .subject("Credit Notification")
                    .recipient(destinationAccountUser.getEmail())
                    .messageBody("The sum of " + transferAmount + " has been credited to your account from " + sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + ". Your current balance is " + destinationAccountUser.getAccountBalance())
                    .build();
            emailServiceRepository.sendEmailAlert(creditAlert);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed. Please try again.");
        }

        // Build and return the success response
        BankRespons bankRespons = BankRespons.builder()
                .responseCode(AccountUtilities.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(AccountUtilities.TRANSFER_SUCCESSFUL_MESSAGE)
                .accountInfo(null)
                .build();
        return new ResponseEntity<>(bankRespons, HttpStatus.OK);
    }
}
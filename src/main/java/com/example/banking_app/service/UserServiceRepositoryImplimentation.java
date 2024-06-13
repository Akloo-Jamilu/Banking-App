package com.example.banking_app.service;
import com.example.banking_app.dto.EmailDetails;
import com.example.banking_app.dto.EnquiryDto;
import com.example.banking_app.dto.TransactionDto;
import com.example.banking_app.dto.UserDto;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.USerRepository;
import com.example.banking_app.respons.AccountInfo;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.service.servicesRepository.EmailServiceRepository;
import com.example.banking_app.service.servicesRepository.UserServiceRepository;
import com.example.banking_app.utils.AccountUtilities;
import com.example.banking_app.utils.UserValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
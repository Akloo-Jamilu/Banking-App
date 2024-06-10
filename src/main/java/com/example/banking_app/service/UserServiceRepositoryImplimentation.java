package com.example.banking_app.service;
import com.example.banking_app.dto.EmailDetails;
import com.example.banking_app.dto.EnquiryDto;
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
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.math.BigDecimal;
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
                        .accountBalance(String.valueOf(saveUser.getAccountBalance()))
                        .aacountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName() + " " + saveUser.getLastName() + " " + saveUser.getOtherNme())
                        .build())
                .build();
    }

    @Override
    public BankRespons balanceEnquiry(EnquiryDto enquiryDto) {
        boolean isAccountExist = uSerRepository.existsByAccountNumber(enquiryDto.getAccountNumber());
        if (!isAccountExist){
            return BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = uSerRepository.findByAccountNumber(enquiryDto.getAccountNumber());


    }

    @Override
    public String nameEnquiry(EnquiryDto enquiryDto) {
        return null;
    }

}
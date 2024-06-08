package com.example.banking_app.service.impl;
import com.example.banking_app.dto.UserDto;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.USerRepository;
import com.example.banking_app.respons.AccountInfo;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.utils.AccountUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.math.BigDecimal;
import java.util.Set;

@Service
public class UserServiceImplimentation implements UserService {

    private final USerRepository uSerRepository;
    private final Validator validator;

    @Autowired
    public UserServiceImplimentation(USerRepository uSerRepository, Validator validator) {
        this.uSerRepository = uSerRepository;
        this.validator = validator;
    }

    @Override
    public BankRespons createAccount(UserDto userDto) {
        // Validate UserDto
        validateUserDto(userDto);

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

    private void validateUserDto(UserDto userDto) {
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        if (!violations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<UserDto> violation : violations) {
                stringBuilder.append(violation.getMessage()).append("; ");
            }
            throw new ConstraintViolationException("Validation failed: " + stringBuilder.toString(), violations);
        }
    }
}
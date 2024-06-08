package com.example.banking_app.service.impl;

import com.example.banking_app.dto.UserDto;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.USerRepository;
import com.example.banking_app.respons.AccountInfo;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.utils.AccountUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImplimentation implements UserService{

    @Autowired
    USerRepository uSerRepository;

    @Override
    public BankRespons createAccount(UserDto userDto) {
        /**
         * saving new user to database
         * */
//        check if user exist
        if (uSerRepository.existsByEmail(userDto.getEmail())){
            return  BankRespons.builder()
                    .responseCode(AccountUtilities.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtilities.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

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
        User saveUser = uSerRepository.save(user);
        return BankRespons.builder()
                .responseCode(AccountUtilities.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtilities.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(saveUser.getAccountNumber())
                        .aacountNumber(saveUser.getAccountNumber())
                        .accountName(saveUser.getFirstName()+ " " + saveUser.getFirstName()+ " " + saveUser.getOtherNme())
                        .build())
                .build();

    }
}

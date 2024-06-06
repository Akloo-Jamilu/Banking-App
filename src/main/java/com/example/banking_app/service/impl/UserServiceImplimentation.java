package com.example.banking_app.service.impl;

import com.example.banking_app.dto.UserDto;
import com.example.banking_app.entity.User;
import com.example.banking_app.respons.BankRespons;

public class UserServiceImplimentation implements UserService{


    @Override
    public BankRespons createAccount(UserDto userDto) {
        /**
         * saving new user to database
         * */

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .otherNme(userDto.getOtherNme())
                .gender(userDto.getGender())
                .address(userDto.getAddress())
                .stateOfOrigin(userDto.getStateOfOrigin())
                .accountNumber()
                .build();
    }
}

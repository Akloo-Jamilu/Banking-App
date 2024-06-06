package com.example.banking_app.service.impl;

import com.example.banking_app.dto.UserDto;
import com.example.banking_app.respons.BankRespons;

public interface UserService {
    BankRespons createAccount(UserDto userDto);
}

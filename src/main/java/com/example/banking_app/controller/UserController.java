package com.example.banking_app.controller;

import com.example.banking_app.dto.UserDto;
import com.example.banking_app.respons.BankRespons;
import com.example.banking_app.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public BankRespons createAccount(@RequestBody UserDto userDto){
        return userService.createAccount(userDto);

    }
}

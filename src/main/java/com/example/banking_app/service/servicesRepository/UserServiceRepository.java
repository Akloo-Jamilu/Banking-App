package com.example.banking_app.service.servicesRepository;

import com.example.banking_app.dto.EnquiryDto;
import com.example.banking_app.dto.UserDto;
import com.example.banking_app.respons.BankRespons;

public interface UserServiceRepository {
    BankRespons createAccount(UserDto userDto);
    BankRespons balanceEnquiry(EnquiryDto enquiryDto);
    String nameEnquiry(EnquiryDto enquiryDto);
    BankRespons
}

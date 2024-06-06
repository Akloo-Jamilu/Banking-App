package com.example.banking_app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class User {
    private Long id;
    private String firstName;
    private String LastName;
    private String otherNme;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private String accountNumber;
    private BigDecimal accountBalance;
    private String email;
    private String phoneNumber;
    private String alternativePhoneNumber;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

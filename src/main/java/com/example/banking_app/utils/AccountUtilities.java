package com.example.banking_app.utils;

import java.time.Year;
import java.util.HashSet;

public class AccountUtilities {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";
    public static final String ACCOUNT_CREATION_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "User created successfully";
    public static final String ACCOUNT_NOT_EXIST_CODE = "03";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "No user with that Account Number";
    public static final String ACCOUNT_FOUND_CODE = "04";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account fetched successfully";
    public static final String ACCOUNT_CREDIT_CODE = "05";
    public static final String ACCOUNT_CREDIT_MESSAGE = "Account credited successfully";


   public static String generateAccountNumber(){

       Year PresentYear = Year.now();
        int min = 100000; // Smallest 6-digit number
       int max = 999999; // Largest 6-digit number


       int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

       String yearString = String.valueOf(PresentYear);
       String randNumberString = String.valueOf(randNumber);

       StringBuilder accountNumber = new StringBuilder();
       accountNumber.append(PresentYear).append(randNumber).toString();

       return yearString + randNumberString;
   }
}


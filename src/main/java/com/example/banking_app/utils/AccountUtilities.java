package com.example.banking_app.utils;

import java.time.Year;
import java.util.HashSet;

public class AccountUtilities {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";

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


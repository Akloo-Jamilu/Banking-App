package com.example.banking_app.utils;

import org.springframework.http.HttpStatus;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class AccountUtilities {
    public static final String ACCOUNT_EXISTS_CODE = "ACCOUNT_EXISTS";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account";

    public static final String ACCOUNT_CREATION_CODE = "ACCOUNT_CREATED";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account created successfully";

    public static final String ACCOUNT_FOUND_CODE = "ACCOUNT_FOUND";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account fetched successfully";

    public static final String ACCOUNT_NAME_FOUND_CODE = "ACCOUNT_NAME_FOUND";
    public static final String ACCOUNT_NAME_FOUND_MESSAGE = "Account name fetched successfully";

    public static final String ACCOUNT_INSUFFICIENT_BALANCE_CODE = "INSUFFICIENT_BALANCE";
    public static final String ACCOUNT_INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";

    public static final String ACCOUNT_NOT_EXIST_CODE = "ACCOUNT_NOT_EXIST";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account does not exist";

    public static final String ACCOUNT_DEBITED_CODE = "ACCOUNT_DEBITED";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account debited successfully";

    public static final String ACCOUNT_CREDITED_CODE = "ACCOUNT_CREDITED";
    public static final String ACCOUNT_CREDITED_MESSAGE = "Account credited successfully";
    public static final String TRANSFER_SUCCESSFUL_CODE = "TRANSFER_SUCCESSFUL";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Your Transfer was successful";

    private static final Map<String, HttpStatus> RESPONSE_CODE_TO_HTTP_STATUS_MAP = new HashMap<>();

    static {
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_EXISTS_CODE, HttpStatus.BAD_REQUEST);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_CREATION_CODE, HttpStatus.OK);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_INSUFFICIENT_BALANCE_CODE, HttpStatus.BAD_REQUEST);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_NOT_EXIST_CODE, HttpStatus.NOT_FOUND);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_DEBITED_CODE, HttpStatus.OK);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_CREDITED_CODE, HttpStatus.OK);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_FOUND_CODE, HttpStatus.OK);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(ACCOUNT_NAME_FOUND_CODE, HttpStatus.OK);
        RESPONSE_CODE_TO_HTTP_STATUS_MAP.put(TRANSFER_SUCCESSFUL_CODE, HttpStatus.OK);

    }

    public static HttpStatus getHttpStatusForResponseCode(String responseCode) {
        return RESPONSE_CODE_TO_HTTP_STATUS_MAP.getOrDefault(responseCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }




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


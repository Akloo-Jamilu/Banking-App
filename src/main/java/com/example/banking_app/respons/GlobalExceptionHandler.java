package com.example.banking_app.respons;
import com.example.banking_app.respons.BankRespons;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<BankRespons> handleConstraintViolationException(ConstraintViolationException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        ex.getConstraintViolations().forEach(violation -> errorMessage.append(violation.getMessage()).append("; "));

        BankRespons response = BankRespons.builder()
                .responseCode("VALIDATION_ERROR")
                .responseMessage(errorMessage.toString())
                .accountInfo(null)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // You can add more exception handlers here if needed
}
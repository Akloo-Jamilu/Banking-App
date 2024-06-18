package com.example.banking_app.service;

import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@AllArgsConstructor
public class BankStatementServiceRepositoryImplementation {
    private TransactionRepository transactionRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate ){
        // Trim the date strings to remove any leading or trailing whitespace
        LocalDate startingFrom = LocalDate.parse(startDate.trim(), DateTimeFormatter.ISO_DATE);
        LocalDate endingIn = LocalDate.parse(endDate.trim(), DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDay = startingFrom.atStartOfDay();
        LocalDateTime endOfDay = endingIn.plusDays(1).atStartOfDay().minusSeconds(1);

        return transactionRepository.findAll().stream()
//                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
//                .filter(transaction -> transaction.getCreatedAt().isEqual(startOfDay)).
//                filter(transaction -> transaction.getCreatedAt().isEqual(endOfDay)).toList();

                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(startOfDay) && !transaction.getCreatedAt().isAfter(endOfDay))
                .toList();
    }
}

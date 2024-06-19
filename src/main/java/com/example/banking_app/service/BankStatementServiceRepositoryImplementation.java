package com.example.banking_app.service;

import com.example.banking_app.entity.Transaction;
import com.example.banking_app.repository.TransactionRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
@AllArgsConstructor
@Slf4j
public class BankStatementServiceRepositoryImplementation {
    private TransactionRepository transactionRepository;
    private static final String FILE = "C:\\Users\\Admin\\Document\\MyStatement.pdf";

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

    private void designStatement(List<Transaction> transactions){
        Rectangle rectangle = new Rectangle(PageSize.A4);
        Document document = new Document(rectangle);
        log.info("setting paper size");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document , outputStream);

    }


}

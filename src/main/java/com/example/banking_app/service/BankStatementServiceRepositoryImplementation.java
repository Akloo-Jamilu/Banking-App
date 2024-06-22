package com.example.banking_app.service;

import com.example.banking_app.entity.Transaction;
import com.example.banking_app.entity.User;
import com.example.banking_app.repository.TransactionRepository;
import com.example.banking_app.repository.USerRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
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
    private USerRepository uSerRepository;
    private static final String FILE = "C:\\Users\\Admin\\Document\\MyStatement.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate ) throws DocumentException, FileNotFoundException {
        // Trim the date strings to remove any leading or trailing whitespace
        LocalDate startingFrom = LocalDate.parse(startDate.trim(), DateTimeFormatter.ISO_DATE);
        LocalDate endingIn = LocalDate.parse(endDate.trim(), DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDay = startingFrom.atStartOfDay();
        LocalDateTime endOfDay = endingIn.plusDays(1).atStartOfDay().minusSeconds(1);

        User user = uSerRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName()+ " " + user.getLastName()+ " " + user.getOtherNme();

        Rectangle rectangle = new Rectangle(PageSize.A4);
        Document document = new Document(rectangle);
        log.info("setting paper size");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document , outputStream);
        document.open();

        PdfPTable bankHeadingTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Banking Application"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("No. 89, Aguyi Iro-sin Boulevard"));
        bankAddress.setBorder(0);
        bankHeadingTable.addCell(bankName);
        bankHeadingTable.addCell(bankAddress);

        PdfPTable statementInfoSection = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell( new Phrase("Start Date" +" "+startOfDay ));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date" +" "+endOfDay ));
        stopDate.setBorder(0);

        PdfPCell name = new PdfPCell(new Phrase("Customer Name" + " "+customerName));
        name.setBorder(0);

        PdfPCell address = new PdfPCell(new Phrase("Customer Address" + " " + user.getAddress()));
        address.setBorder(0);





        return transactionRepository.findAll().stream()
//                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
//                .filter(transaction -> transaction.getCreatedAt().isEqual(startOfDay)).
//                filter(transaction -> transaction.getCreatedAt().isEqual(endOfDay)).toList();

                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> !transaction.getCreatedAt().isBefore(startOfDay) && !transaction.getCreatedAt().isAfter(endOfDay))
                .toList();
    }




}

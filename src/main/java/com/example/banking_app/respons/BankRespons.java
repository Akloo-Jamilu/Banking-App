package com.example.banking_app.respons;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankRespons {
    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
}

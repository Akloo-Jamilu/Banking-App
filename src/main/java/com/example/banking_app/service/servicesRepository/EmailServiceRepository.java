package com.example.banking_app.service.servicesRepository;

import com.example.banking_app.dto.EmailDetails;

public interface EmailServiceRepository {
    void sendEmailAlert(EmailDetails emailDetails);
}

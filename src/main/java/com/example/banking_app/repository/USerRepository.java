package com.example.banking_app.repository;

import com.example.banking_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface USerRepository  extends JpaRepository<User, Long> {
        Boolean existsByEmail(String email);
        Boolean existsByAccountNumber(String accountNumber);
        User findByAccountNumber(String accountNumber);
}

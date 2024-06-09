package com.example.banking_app.utils;

import com.example.banking_app.dto.UserDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;


import java.util.Set;

public class UserValidations {

    private final Validator validator;

    public UserValidations(Validator validator) {
        this.validator = validator;
    }

    public void validateUserDto(UserDto userDto) {
            Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
            if (!violations.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                for (ConstraintViolation<UserDto> violation : violations) {
                    stringBuilder.append(violation.getMessage()).append("; ");
                }
                throw new ConstraintViolationException("Validation failed: " + stringBuilder.toString(), violations);
            }
        }

}

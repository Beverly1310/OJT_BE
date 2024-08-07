package com.example.ojt.validation.Impl;

import com.example.ojt.model.dto.request.PasswordChangeRequest;
import com.example.ojt.validation.PasswordsMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, PasswordChangeRequest> {

    @Override
    public boolean isValid(PasswordChangeRequest request, ConstraintValidatorContext context) {
        if (request.getNewPassword() == null || request.getRepeatPassword() == null) {
            return false;
        }
        boolean isValid= request.getNewPassword().equals(request.getRepeatPassword());
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Incorrect password repetition!")
                    .addPropertyNode("repeatPassword")
                    .addConstraintViolation();
        }
        return isValid;
    }
}
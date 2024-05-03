package com.users.api.util.validator.user;

import com.users.api.util.validator.Text;
import com.users.api.util.validator.ValidationMessages;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

import static com.users.api.util.Guard.guard;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public void initialize(ValidUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        try {
            guard(Text.of(username)).againstNullOrWhitespace(ValidationMessages.CANNOT_BE_NULL_OR_EMPTY);
        } catch (ValidationException exception) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(exception.getMessage()).addConstraintViolation();

            return false;
        }

        return true;
    }
}

package com.users.api.util.validator.password;

import com.users.api.util.validator.Text;
import com.users.api.util.validator.ValidationMessages;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

import static com.users.api.util.Guard.guard;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        try {
            guard(Text.of(password)).againstNullOrWhitespace(ValidationMessages.CANNOT_BE_NULL_OR_EMPTY);
            guard(password).againstLength(ValidationMessages.PASSWORD_CANNOT_BE_INFERIOR);
        } catch (ValidationException exception) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(exception.getMessage()).addConstraintViolation();

            return false;
        }

        return true;
    }
}

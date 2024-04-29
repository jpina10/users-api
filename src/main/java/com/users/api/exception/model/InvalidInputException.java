package com.users.api.exception.model;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidInputException extends ValidationException {

    public InvalidInputException(String value) {
        super(value);
    }
}

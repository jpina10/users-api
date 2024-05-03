package com.users.api.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PROCESSING)
public class AccessException extends RuntimeException {

    private static final String CANNOT_ACCESS_THE_FIELD = "Cannot access the field %s.";

    public AccessException(String value) {
        super(String.format(CANNOT_ACCESS_THE_FIELD, value));
    }
}

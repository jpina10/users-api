package com.users.api.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class SecurityInputValidationException extends RuntimeException {

    public SecurityInputValidationException(String message){
        super(message);
    }
}

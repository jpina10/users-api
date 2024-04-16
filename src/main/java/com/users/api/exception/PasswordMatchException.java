package com.users.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordMatchException extends RuntimeException {

    public PasswordMatchException(String message){
        super(message);
    }
}

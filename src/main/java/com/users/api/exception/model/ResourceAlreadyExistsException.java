package com.users.api.exception.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {
    public static final String RESOURCE_ALREADY_EXISTS = "The user with username %s already exists.";

    public ResourceAlreadyExistsException(String value){
        super(String.format(RESOURCE_ALREADY_EXISTS, value));
    }
}

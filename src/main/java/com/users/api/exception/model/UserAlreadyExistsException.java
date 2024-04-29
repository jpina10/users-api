package com.users.api.exception.model;

public class UserAlreadyExistsException extends ResourceAlreadyExistsException{

    public static final String RESOURCE_ALREADY_EXISTS = "The user with username %s already exists.";

    public UserAlreadyExistsException(String value) {
        super(String.format(RESOURCE_ALREADY_EXISTS, value));
    }
}

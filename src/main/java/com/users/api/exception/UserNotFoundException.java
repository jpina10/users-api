package com.users.api.exception;

public class UserNotFoundException extends ResourceNotFoundException{

    private static final String USERNAME_NOT_FOUND = "User with username %s does not exist.";

    public UserNotFoundException(String value) {
        super(String.format(USERNAME_NOT_FOUND, value));
    }
}

package com.users.api.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String value) {
        super(value);
    }
}

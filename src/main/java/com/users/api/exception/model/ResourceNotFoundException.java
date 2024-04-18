package com.users.api.exception.model;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String value) {
        super(value);
    }
}

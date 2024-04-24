package com.users.api.util.validator;

public record Text(String value) {

    public boolean isNullOrWhitespace() {
        return value == null || value.trim().isEmpty();
    }
}

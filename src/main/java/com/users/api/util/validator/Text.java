package com.users.api.util.validator;

public record Text(String value) {

    public boolean isNullOrWhitespace() {
        return value == null || value.trim().isEmpty();
    }

    public static Text of(String value) {
        return new Text(value);
    }
}

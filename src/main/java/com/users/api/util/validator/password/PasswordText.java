package com.users.api.util.validator.password;

public record PasswordText(String value) {

    public boolean isLengthInvalid() {
        return value.length() < 6;
    }

    public static PasswordText of(String value) {
        return new PasswordText(value);
    }
}

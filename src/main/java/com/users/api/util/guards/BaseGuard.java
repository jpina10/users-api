package com.users.api.util.guards;

import jakarta.validation.ValidationException;

import java.util.function.BooleanSupplier;

public class BaseGuard<T> {
    protected final T value;

    public BaseGuard(T value) {
        this.value = value;
    }

    protected void against(BooleanSupplier tester, String message) {
        if (tester.getAsBoolean()) {
            throw new ValidationException(message);
        }
    }

}

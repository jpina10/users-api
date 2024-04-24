package com.users.api.util.guards;

import com.users.api.util.validator.password.PasswordText;

public class PasswordGuard extends BaseGuard<PasswordText> {
    public PasswordGuard(PasswordText value) {
        super(value);
    }

    public void againstLength(String message) {
        against(value::isLengthInvalid, message);
    }
}

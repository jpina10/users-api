package com.users.api.util;

import com.users.api.util.guards.ObjectGuard;
import com.users.api.util.guards.PasswordGuard;
import com.users.api.util.guards.TextGuard;
import com.users.api.util.validator.Text;
import com.users.api.util.validator.password.PasswordText;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Guard {

    public static PasswordGuard guard(String value) {
        return new PasswordGuard(new PasswordText(value));
    }

    public static TextGuard guard(Text value) {
        return new TextGuard(value);
    }

    public static ObjectGuard guard(Object value) {
        return new ObjectGuard(value);
    }
}

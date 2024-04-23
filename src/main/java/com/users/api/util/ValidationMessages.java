package com.users.api.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationMessages {
    public static final String CANNOT_BE_NULL = "cannot be null";
    public static final String CANNOT_BE_EMPTY = "cannot be empty";
    public static final String CANNOT_BE_BLANK = "cannot be blank";
    public static final String CANNOT_BE_NULL_OR_EMPTY = "cannot be null or empty";
    public static final String PASSWORD_CANNOT_BE_INFERIOR = "cannot be inferior of 6 characters";
}

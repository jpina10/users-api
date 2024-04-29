package com.users.api.util.log;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoggingMessages {
    public static final String SAVING = "saving user with username {}";
    public static final String RETRIEVING = "Retrieving user with username: {}";
    public static final String DELETING = "deleting user with username {}";
    public static final String ORIGINAL_USER = "deleting user with username {}";
    public static final String PATCHED_USER = "deleting user with username {}";
    public static final String CALLING_RANDOM_USER_API = "calling random user API...";
}

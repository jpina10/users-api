package com.users.api.exception;

import org.springframework.http.HttpStatus;

public record RestErrorMessage(HttpStatus httpStatus, String message) {
}

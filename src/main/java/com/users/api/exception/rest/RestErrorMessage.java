package com.users.api.exception.rest;

import org.springframework.http.HttpStatus;

public record RestErrorMessage(int status, HttpStatus error, String message) {
}

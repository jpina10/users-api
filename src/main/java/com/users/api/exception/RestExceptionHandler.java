package com.users.api.exception;

import com.users.api.exception.model.ResourceNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    private static final String SPACE = " ";
    private static final String DELIMITER = ", ";

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<RestErrorMessage> resourceNotFoundHandler(ResourceNotFoundException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return new ResponseEntity<>(restErrorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<RestErrorMessage> invalidInputHandler(ValidationException exception) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return new ResponseEntity<>(restErrorMessage, HttpStatus.BAD_REQUEST);
    }

    //in case of use jakarta.validation annotations
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<RestErrorMessage> invalidInput(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + SPACE + fieldError.getDefaultMessage())
                .collect(Collectors.joining(DELIMITER));

        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST, message);

        return new ResponseEntity<>(restErrorMessage, HttpStatus.BAD_REQUEST);
    }
}

package com.users.api.exception;

import com.users.api.exception.model.AccessException;
import com.users.api.exception.model.ResourceAlreadyExistsException;
import com.users.api.exception.model.ResourceNotFoundException;
import com.users.api.exception.rest.RestErrorMessage;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    private static final String SPACE = " ";
    private static final String DELIMITER = ", ";

    @ExceptionHandler({ResourceNotFoundException.class})
    private ResponseEntity<RestErrorMessage> resourceNotFoundHandler(ResourceNotFoundException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    private ResponseEntity<RestErrorMessage> illegalAccessHandler(ResourceAlreadyExistsException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(AccessException.class)
    private ResponseEntity<RestErrorMessage> illegalAccessHandler(AccessException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<RestErrorMessage> invalidInputHandler(MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + SPACE + fieldError.getDefaultMessage())
                .collect(Collectors.joining(DELIMITER));

        RestErrorMessage restErrorMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, message);

        return new ResponseEntity<>(restErrorMessage, restErrorMessage.error());
    }

    private ResponseEntity<RestErrorMessage> handleException(Exception exception) {
        HttpStatus httpStatus = getHttpStatus(exception);

        RestErrorMessage restErrorMessage = new RestErrorMessage(httpStatus.value(), httpStatus, exception.getMessage());

        return new ResponseEntity<>(restErrorMessage, restErrorMessage.error());
    }

    private static HttpStatus getHttpStatus(Exception exception) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);

        return responseStatus != null ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

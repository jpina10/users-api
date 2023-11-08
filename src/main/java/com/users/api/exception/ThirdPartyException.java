package com.users.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ThirdPartyException extends RuntimeException {

    public ThirdPartyException(String message){
        super(message);
    }
}

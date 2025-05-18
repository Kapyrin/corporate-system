package ru.roznov.rabbitservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestParameterException extends RuntimeException {
    public InvalidRequestParameterException(String paramName, String message) {
        super("Invalid parameter '" + paramName + "': " + message);
    }
}
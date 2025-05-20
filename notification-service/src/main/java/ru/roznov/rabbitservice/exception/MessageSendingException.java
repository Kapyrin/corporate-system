package ru.roznov.rabbitservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MessageSendingException extends RuntimeException {
    public MessageSendingException(String queueName, Throwable cause) {
        super("Failed to send message to " + queueName, cause);
    }
}
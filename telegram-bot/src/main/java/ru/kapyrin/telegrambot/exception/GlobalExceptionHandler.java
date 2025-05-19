package ru.kapyrin.telegrambot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyRegisteredException.class)
    public ResponseEntity<String> handleAlreadyRegistered(AlreadyRegisteredException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<String> handleNotificationError(NotificationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(" Failed to process notification: " + ex.getMessage());
    }


}
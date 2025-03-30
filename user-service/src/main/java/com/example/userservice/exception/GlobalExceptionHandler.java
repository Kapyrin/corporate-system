package com.example.userservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        return ResponseEntity.status(500).body(createErrorResponse(ex.getMessage(), 500));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(createErrorResponse(ex.getMessage(), 404));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(409).body(createErrorResponse(ex.getMessage(), 409));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(409).body(createErrorResponse("Database error: " + ex.getRootCause().getMessage(), 409));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("errors", errors);
        errorResponse.put("status", 422);

        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }

    private Map<String, Object> createErrorResponse(String message, int status) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("timestamp", LocalDateTime.now());
        errorMap.put("error", message);
        errorMap.put("status", status);
        return errorMap;
    }
}

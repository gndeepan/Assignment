package com.assignment.task.Assignment.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.Getter;
import lombok.Setter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFoundException(TaskNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for request",
                LocalDateTime.now(),
                errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid request format";
        Map<String, String> errors = new HashMap<>();

        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatEx = (InvalidFormatException) ex.getCause();
            String fieldName = invalidFormatEx.getPath().isEmpty() ? "unknown"
                    : invalidFormatEx.getPath().get(invalidFormatEx.getPath().size() - 1).getFieldName();

            // If it's an enum, provide the allowed values
            if (invalidFormatEx.getTargetType().isEnum()) {
                Object[] enumValues = invalidFormatEx.getTargetType().getEnumConstants();
                String allowedValues = String.join(", ", convertEnumToStringArray(enumValues));

                message = "Invalid value for field '" + fieldName + "'";
                errors.put(fieldName, "Value '" + invalidFormatEx.getValue() +
                        "' is not valid. Allowed values are: " + allowedValues);
            } else {
                message = "Invalid format for field '" + fieldName + "'";
                errors.put(fieldName, "Value '" + invalidFormatEx.getValue() +
                        "' is not valid for type " + invalidFormatEx.getTargetType().getSimpleName());
            }
        } else {
            message = "Invalid request format: " + ex.getMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now(),
                errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private String[] convertEnumToStringArray(Object[] enumConstants) {
        String[] result = new String[enumConstants.length];
        for (int i = 0; i < enumConstants.length; i++) {
            result[i] = enumConstants[i].toString();
        }
        return result;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();

        String message;
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            Object[] enumValues = ex.getRequiredType().getEnumConstants();
            String allowedValues = String.join(", ", convertEnumToStringArray(enumValues));

            message = "Invalid value for parameter '" + ex.getName() + "'";
            errors.put(ex.getName(), "Value '" + ex.getValue() +
                    "' is not valid. Allowed values are: " + allowedValues);
        } else {
            message = "Parameter '" + ex.getName() + "' should be of type '" +
                    (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown") + "'";
            errors.put(ex.getName(), message);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now(),
                errors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid argument: " + ex.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred: " + ex.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Setter
    @Getter
    public static class ErrorResponse {
        private final int status;
        private final String message;
        private final LocalDateTime timestamp;
        private Map<String, String> errors;

        public ErrorResponse(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }

        public ErrorResponse(int status, String message, LocalDateTime timestamp, Map<String, String> errors) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
            this.errors = errors;
        }

    }
}

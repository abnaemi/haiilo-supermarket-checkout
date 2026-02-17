package com.haiilo.interview.haiilosupermarketcheckout.api;

import com.haiilo.interview.haiilosupermarketcheckout.api.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(EntityNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return createResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("Validation failed: {}", errorMessage);
        return createResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneral(Exception ex) {
        log.error("Unexpected error occurred: ", ex);
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponseDTO> createResponse(HttpStatus status, String message) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return new ResponseEntity<>(error, status);
    }
}
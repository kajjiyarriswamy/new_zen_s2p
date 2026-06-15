package com.pr.exception;

import com.pr.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PrNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handlePrNotFound(
            PrNotFoundException ex) {

        logger.warn("PR Not Found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound(ex.getMessage()));
    }
    @ExceptionHandler(PrValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            PrValidationException ex) {

        logger.warn("Validation Failed: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPrIdException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidId(
            InvalidPrIdException ex) {

        logger.warn("Invalid PR ID: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.badRequest(ex.getMessage()));
    }
}
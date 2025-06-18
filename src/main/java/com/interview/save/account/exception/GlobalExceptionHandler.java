package com.interview.save.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler
{
    /**
     * Handles validation errors from @Valid annotation.
     * This catches MethodArgumentNotValidException for DTO validation.
     *
     * @param ex The MethodArgumentNotValidException.
     * @return ResponseEntity with error details and HTTP status 400 (Bad Request).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(final MethodArgumentNotValidException ex)
    {
        final List<String> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

        final Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("messages", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom ResourceNotFoundException.
     *
     * @param ex The ResourceNotFoundException.
     * @return ResponseEntity with error details and HTTP status 404 (Not Found).
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex)
    {
        final Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles custom AccountLimitExceededException.
     *
     * @param ex The AccountLimitExceededException.
     * @return ResponseEntity with error details and HTTP status 400 (Bad Request).
     */
    @ExceptionHandler(AccountLimitExceededException.class)
    public ResponseEntity<Object> handleAccountLimitExceededException(final AccountLimitExceededException ex)
    {
        final Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom OffensiveNicknameException.
     *
     * @param ex The OffensiveNicknameException.
     * @return ResponseEntity with error details and HTTP status 400 (Bad Request).
     */
    @ExceptionHandler(OffensiveNicknameException.class)
    public ResponseEntity<Object> handleOffensiveNicknameException(final OffensiveNicknameException ex)
    {
        final Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles custom DatabaseOperationException.
     *
     * @param ex The DatabaseOperationException.
     * @return ResponseEntity with error details and HTTP status 500 (Internal Server Error).
     */
    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<Object> handleDatabaseOperationException(DatabaseOperationException ex)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Database Error");
        body.put("message", ex.getMessage());

        // Optionally log the original cause: ex.getCause()
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles general ValidationException for cases not covered by MethodArgumentNotValidException.
     *
     * @param ex The ValidationException.
     * @return ResponseEntity with error details and HTTP status 400 (Bad Request).
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleCustomValidationException(ValidationException ex)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles any other unexpected exceptions.
     * This is a fallback handler for uncaught exceptions.
     *
     * @param ex The unexpected Exception.
     * @return ResponseEntity with error details and HTTP status 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex)
    {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred: " + ex.getMessage()); // Provide a general message

        // Log the exception for debugging purposes
        ex.printStackTrace();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

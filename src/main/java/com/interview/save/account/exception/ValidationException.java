package com.interview.save.account.exception;

// This exception does not need @ResponseStatus as it will be handled
// by the @ControllerAdvice, which will determine the HTTP status.
public class ValidationException extends RuntimeException
{
    public ValidationException(String message)
    {
        super(message);
    }
}

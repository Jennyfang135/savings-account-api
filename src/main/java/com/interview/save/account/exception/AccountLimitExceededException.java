package com.interview.save.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountLimitExceededException extends RuntimeException
{
    public AccountLimitExceededException(final String message)
    {
        super(message);
    }
}

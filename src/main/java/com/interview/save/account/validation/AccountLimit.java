package com.interview.save.account.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AccountLimitValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccountLimit
{
    String message() default "Customer has reached the maximum allowed number of accounts (5)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.interview.save.account.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OffensiveNicknameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface OffensiveNickname
{
    String message() default "Account nickname contains offensive language";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

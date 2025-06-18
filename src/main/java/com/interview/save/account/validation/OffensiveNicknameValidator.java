package com.interview.save.account.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class OffensiveNicknameValidator implements ConstraintValidator<OffensiveNickname, String> {

    // A simple list of offensive nicknames for demonstration.
    // In a real application, this would be loaded from a configuration, database, or external service.
    private static final List<String> OFFENSIVE_NICKNAMES = Arrays.asList(
        "swearword", "badword", "offensive", "hate", "dummy", "idiot", "stupid"
    );

    @Override
    public void initialize(final OffensiveNickname constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(final String nickname, final ConstraintValidatorContext context) {
        if (nickname == null || nickname.trim().isEmpty()) {
            return true; // Null or empty nicknames are handled by @NotBlank or @Size
        }
        // Check if the nickname, case-insensitively, is in the offensive list
        return OFFENSIVE_NICKNAMES.stream()
            .noneMatch(offensive -> nickname.equalsIgnoreCase(offensive));
    }
}

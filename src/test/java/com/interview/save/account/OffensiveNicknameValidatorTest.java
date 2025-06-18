package com.interview.save.account;

import com.interview.save.account.validation.OffensiveNicknameValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OffensiveNicknameValidatorTest {

    private OffensiveNicknameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new OffensiveNicknameValidator();
        // The validator's initialize method doesn't do anything for this specific validator,
        // but it's good practice to call it if it were relevant.
        // validator.initialize(null); // No annotation instance needed for this validator
    }

    @Test
    void isValid_ShouldReturnFalse_ForOffensiveNickname() {
        assertFalse(validator.isValid("swearword", null), "Should be false for a known offensive word");
        assertFalse(validator.isValid("badword", null), "Should be false for another known offensive word");
        assertFalse(validator.isValid("IDiot", null), "Should be false for offensive word (case insensitive)");
    }

    @Test
    void isValid_ShouldReturnTrue_ForNonOffensiveNickname() {
        assertTrue(validator.isValid("HappyAccount", null), "Should be true for a normal nickname");
        assertTrue(validator.isValid("MySavings", null), "Should be true for another normal nickname");
        assertTrue(validator.isValid("TestAccount123", null), "Should be true for an alphanumeric nickname");
    }

    @Test
    void isValid_ShouldReturnTrue_ForNullNickname() {
        // As per the validator's logic, null is considered valid (handled by @NotBlank/Size on DTO)
        assertTrue(validator.isValid(null, null), "Should be true for null nickname (validation handled elsewhere)");
    }

    @Test
    void isValid_ShouldReturnTrue_ForEmptyNickname() {
        // As per the validator's logic, empty string is considered valid (handled by @NotBlank/Size on DTO)
        assertTrue(validator.isValid("", null), "Should be true for empty nickname (validation handled elsewhere)");
    }

    @Test
    void isValid_ShouldReturnTrue_ForNicknameWithSpaces() {
        assertTrue(validator.isValid("  My Fun Account  ", null), "Should be true for nickname with spaces");
    }

    @Test
    void isValid_ShouldReturnTrue_ForNicknameContainingOffensiveWordButNotMatchingExactly() {
        // This tests that it's not a 'contains' check, but an exact match (case-insensitive)
        assertTrue(validator.isValid("not_a_swearword_at_all", null), "Should be true if it contains part but not exact match");
    }
}

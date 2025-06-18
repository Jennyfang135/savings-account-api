package com.interview.save.account;

import com.interview.save.account.repository.SavingsAccountRepository;
import com.interview.save.account.validation.AccountLimitValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// Use @MockitoSettings to configure strictness, which is the correct way for JUnit 5
@ExtendWith(MockitoExtension.class) // Keep MockitoExtension for @Mock and @InjectMocks
@MockitoSettings(strictness = Strictness.LENIENT) // Configure lenient strictness for this test class
class AccountLimitValidatorTest {

    @Mock
    private SavingsAccountRepository accountRepository; // Mock the repository for counting accounts

    @InjectMocks
    private AccountLimitValidator validator; // Inject mocks into the validator

    private final String CUSTOMER_NAME_UNDER_LIMIT = "CustomerA";
    private final String CUSTOMER_NAME_AT_LIMIT = "CustomerB";
    private final String CUSTOMER_NAME_OVER_LIMIT = "CustomerC";
    private final String NEW_CUSTOMER = "NewCustomer";

    @BeforeEach
    void setUp() {
        // Setup mock behavior for countByCustomerName for different scenarios
        when(accountRepository.countByCustomerName(CUSTOMER_NAME_UNDER_LIMIT)).thenReturn(3L); // 3 accounts existing
        when(accountRepository.countByCustomerName(CUSTOMER_NAME_AT_LIMIT)).thenReturn(5L);    // 5 accounts existing (at limit)
        when(accountRepository.countByCustomerName(CUSTOMER_NAME_OVER_LIMIT)).thenReturn(6L);   // 6 accounts existing (over limit - should not happen if validation works)
        when(accountRepository.countByCustomerName(NEW_CUSTOMER)).thenReturn(0L); // New customer
    }

    @Test
    void isValid_ShouldReturnTrue_WhenUnderAccountLimit() {
        assertTrue(validator.isValid(CUSTOMER_NAME_UNDER_LIMIT, null),
            "Should be true if customer has fewer than 5 accounts");
        assertTrue(validator.isValid(NEW_CUSTOMER, null),
            "Should be true if customer has no accounts");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenAtAccountLimit() {
        assertFalse(validator.isValid(CUSTOMER_NAME_AT_LIMIT, null),
            "Should be false if customer already has 5 accounts");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenOverAccountLimit() {
        assertFalse(validator.isValid(CUSTOMER_NAME_OVER_LIMIT, null),
            "Should be false if customer has more than 5 accounts (even if it shouldn't be possible)");
    }

    @Test
    void isValid_ShouldReturnTrue_ForNullCustomerName() {
        // As per the validator's logic, null is considered valid (handled by @NotBlank on DTO)
        assertTrue(validator.isValid(null, null),
            "Should be true for null customer name (validation handled elsewhere)");
    }

    @Test
    void isValid_ShouldReturnTrue_ForEmptyCustomerName() {
        // As per the validator's logic, empty string is considered valid (handled by @NotBlank on DTO)
        assertTrue(validator.isValid("", null),
            "Should be true for empty customer name (validation handled elsewhere)");
    }
}


package com.interview.save.account.validation;

import com.interview.save.account.repository.SavingsAccountRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // Must be a Spring component to enable autowiring
public class AccountLimitValidator implements ConstraintValidator<AccountLimit, String> {

    private static final int MAX_ACCOUNTS_PER_CUSTOMER = 5;

    // Autowired to access the repository for counting accounts
    @Autowired
    private SavingsAccountRepository accountRepository;

    @Override
    public void initialize(AccountLimit constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String customerName, ConstraintValidatorContext context) {
        if (customerName == null || customerName.trim().isEmpty()) {
            return true; // Handled by @NotBlank on customerName
        }

        // Count existing accounts for the customer
        long existingAccountsCount = accountRepository.countByCustomerName(customerName);

        // Check if adding a new account would exceed the limit
        return existingAccountsCount < MAX_ACCOUNTS_PER_CUSTOMER;
    }
}


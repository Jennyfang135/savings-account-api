package com.interview.save.account.service;

import com.interview.save.account.exception.DatabaseOperationException;
import com.interview.save.account.exception.ResourceNotFoundException;
import com.interview.save.account.model.SavingsAccount;
import com.interview.save.account.model.SavingsAccountCreateRequest;
import com.interview.save.account.repository.SavingsAccountRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class SavingsAccountService
{
    private final SavingsAccountRepository accountRepository;
    private final Random random = new Random();

    @Autowired
    public SavingsAccountService(final SavingsAccountRepository accountRepository)
    {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new savings bank account.
     * This method handles account number generation, saves the account.
     *
     * @param request The {@link SavingsAccountCreateRequest} contains customer name and nickname.
     * @return The created {@link SavingsAccount}.
     * @throws DatabaseOperationException if there's an issue with database interaction.
     */
    @CacheEvict(value = "accounts", allEntries = true)
    @Transactional
    public SavingsAccount createAccount(final SavingsAccountCreateRequest request)
    {
        try {
            // AccountLimit validation is done via @AccountLimit on customerName
            // OffensiveNickname validation is done via @OffensiveNickname on accountNickname
            // Generate a unique 10-digit account number
            String accountNumber;
            do {
                accountNumber = generateAccountNumber();
            } while (accountRepository.findByAccountNumber(accountNumber).isPresent()); // Ensure accountNumber uniqueness

            final SavingsAccount account = new SavingsAccount(accountNumber, request.getCustomerName(), request.getAccountNickname());
            return accountRepository.save(account);
        } catch (DataAccessException e) {
            // Catch data access exceptions and wrap them in a custom exception
            throw new DatabaseOperationException("Failed to create account due to database error.", e);
        }
    }

    /**
     * Retrieves a savings bank account by its account number.
     *
     * @param accountNumber The unique account number.
     * @return The found {@link SavingsAccount}.
     * @throws ResourceNotFoundException if no account is found with the given account number.
     * @throws DatabaseOperationException if there's an issue with database interaction.
     */
    @Cacheable(value = "accounts", key = "#accountNumber")
    @Transactional(readOnly = true)
    public SavingsAccount getAccount(final String accountNumber)
    {
        try {
            return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account with number " + accountNumber + " not found."));
        } catch (DataAccessException e) {
            // Catch data access exceptions and wrap them in a custom exception
            throw new DatabaseOperationException("Failed to retrieve account due to database error.", e);
        }
    }

    /**
     * Retrieves all savings bank accounts.
     *
     * @return A list of all {@link SavingsAccount} entities.
     * @throws DatabaseOperationException if there's an issue with database interaction.
     */
    @Cacheable(value = "accounts", key = "'allAccounts'")
    @Transactional(readOnly = true)
    public List<SavingsAccount> getAllAccounts()
    {
        try {
            return accountRepository.findAll();
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve all accounts due to database error.", e);
        }
    }

    /**
     * Deletes a savings bank account by its ID.
     *
     * @param id The unique ID of the account to delete.
     * @return The ID of the deleted account.
     * @throws ResourceNotFoundException if no account is found with the given ID.
     * @throws DatabaseOperationException if there's an issue with database interaction.
     */
    @CacheEvict(value = "accounts", allEntries = true)
    @Transactional
    public String deleteAccountById(final String id)
    {
        try {
            // Check if account exists before trying to delete.
            if (!accountRepository.existsById(id)) {
                throw new ResourceNotFoundException("Account with ID " + id + " not found.");
            }
            accountRepository.deleteById(id);
            return id;
        } catch (EmptyResultDataAccessException e) {
            // This exception is thrown by deleteById if the entity doesn't exist.
            // While we have existsById, this is a good fallback for robustness.
            throw new ResourceNotFoundException("Account with ID " + id + " not found.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to delete account due to database error.", e);
        }
    }

    /**
     * Generates a random 10-digit account number.
     * In a real system, this would be more robust (e.g., UUID, sequence, check digits).
     * @return A string representing a 10-digit account number.
     */
    private String generateAccountNumber()
    {
        // Generate a random 9-digit number and pad with leading zeros if necessary
        // Then add a leading '1' to ensure it's always 10 digits and looks like a bank account
        return String.format("1%09d", random.nextInt(1_000_000_000));
    }
}

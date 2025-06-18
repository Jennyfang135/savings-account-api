package com.interview.save.account.service;

import com.interview.save.account.exception.DatabaseOperationException;
import com.interview.save.account.exception.ResourceNotFoundException;
import com.interview.save.account.model.SavingsAccount;
import com.interview.save.account.model.SavingsAccountCreateRequest;
import com.interview.save.account.repository.SavingsAccountRepository;
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
    public SavingsAccountService(SavingsAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new savings bank account.
     * This method handles account number generation, saves the account,
     * and performs validations.
     *
     * @param request The AccountCreateRequest DTO containing customer name and nickname.
     * @return The created Account entity.
     * @throws DatabaseOperationException if there's an issue with database interaction.
     */
    @Transactional // Ensures atomicity of the operation
    public SavingsAccount createAccount(SavingsAccountCreateRequest request)
    {
        try {
            // AccountLimit validation is done via @AccountLimit on customerName in DTO
            // OffensiveNickname validation is done via @OffensiveNickname on accountNickname in DTO

            // Generate a unique 10-digit account number
            String accountNumber;
            do {
                accountNumber = generateAccountNumber();
            } while (accountRepository.findByAccountNumber(accountNumber).isPresent()); // Ensure uniqueness

            SavingsAccount account = new SavingsAccount(accountNumber, request.getCustomerName(), request.getAccountNickname());
            return (SavingsAccount) accountRepository.save(account);
        } catch (DataAccessException e) {
            // Catch specific data access exceptions and wrap them in a custom exception
            throw new DatabaseOperationException("Failed to create account due to database error.", e);
        } catch (Exception e) {
            // Catch any other unexpected exceptions during the process
            throw new RuntimeException("An unexpected error occurred while creating account.", e);
        }
    }

    /**
     * Retrieves a savings bank account by its account number.
     *
     * @param accountNumber The unique account number.
     * @return The found Account entity.
     * @throws ResourceNotFoundException if no account is found with the given account number.
     * @throws DatabaseOperationException if there's an issue with database interaction.
     */
    @Transactional(readOnly = true) // Optimize for read operations
    public SavingsAccount getAccount(String accountNumber)
    {
        try {
            return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account with number " + accountNumber + " not found."));
        } catch (DataAccessException e) {
            // Catch specific data access exceptions and wrap them in a custom exception
            throw new DatabaseOperationException("Failed to retrieve account due to database error.", e);
        } catch (Exception e) {
            // Catch any other unexpected exceptions during the process
            throw new RuntimeException("An unexpected error occurred while retrieving account.", e);
        }
    }

    /**
     * Retrieves all savings bank accounts.
     *
     * @return A list of all SavingsAccount entities.
     * @throws DatabaseOperationException if there's an issue with database interaction.
     */
    @Transactional(readOnly = true)
    public List<SavingsAccount> getAllAccounts() { // Changed from Account to SavingsAccount
        try {
            return accountRepository.findAll();
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to retrieve all accounts due to database error.", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while retrieving all accounts.", e);
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
    @Transactional
    public String deleteAccountById(String id) { // Changed return type from void to String
        try {
            if (!accountRepository.existsById(id)) { // Check if account exists before trying to delete
                throw new ResourceNotFoundException("Account with ID " + id + " not found.");
            }
            accountRepository.deleteById(id);
            return id; // Return the ID upon successful deletion
        } catch (EmptyResultDataAccessException e) {
            // This exception is thrown by deleteById if the entity doesn't exist.
            // While we have existsById, this is a good fallback for robustness.
            throw new ResourceNotFoundException("Account with ID " + id + " not found.");
        } catch (DataAccessException e) {
            throw new DatabaseOperationException("Failed to delete account due to database error.", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while deleting account.", e);
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

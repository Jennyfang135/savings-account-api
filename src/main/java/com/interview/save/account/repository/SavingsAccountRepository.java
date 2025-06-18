package com.interview.save.account.repository;

import com.interview.save.account.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, String>
{
    /**
     * Finds an account by its unique account number.
     * @param accountNumber The account number to search for.
     * @return An Optional containing the found Account, or empty if not found.
     */
    Optional<SavingsAccount> findByAccountNumber(String accountNumber);

    /**
     * Counts the number of accounts associated with a specific customer name.
     * @param customerName The name of the customer.
     * @return The count of accounts for the given customer.
     */
    long countByCustomerName(String customerName);
}

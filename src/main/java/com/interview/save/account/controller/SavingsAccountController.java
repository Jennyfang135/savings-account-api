package com.interview.save.account.controller;

import com.interview.save.account.model.AccountResponse;
import com.interview.save.account.model.SavingsAccount;
import com.interview.save.account.model.SavingsAccountCreateRequest;
import com.interview.save.account.service.SavingsAccountService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class SavingsAccountController
{
    private final SavingsAccountService accountService;

    @Autowired
    public SavingsAccountController(final SavingsAccountService accountService)
    {
        this.accountService = accountService;
    }

    /**
     * Endpoint to create a new savings bank account.
     * Request body is validated using @Valid.
     *
     * @param request The AccountCreateRequest DTO.
     * @return ResponseEntity with the created {@link AccountResponse} and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody SavingsAccountCreateRequest request)
    {
        final SavingsAccount createdAccount = accountService.createAccount(request);
        final AccountResponse response = new AccountResponse(
            createdAccount.getId(),
            createdAccount.getAccountNumber(),
            createdAccount.getCustomerName(),
            createdAccount.getAccountNickname()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get a savings bank account by its account number.
     *
     * @param accountNumber The unique account number.
     * @return ResponseEntity with the {@link AccountResponse} for the account number.
     */
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber)
    {
        SavingsAccount account = accountService.getAccount(accountNumber);
        AccountResponse response = new AccountResponse(
            account.getId(),
            account.getAccountNumber(),
            account.getCustomerName(),
            account.getAccountNickname()
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to get all savings bank accounts.
     *
     * @return ResponseEntity with a list of {@link AccountResponse}.
     */
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts()
    {
        final List<SavingsAccount> savingsAccountList = accountService.getAllAccounts();
        final List<AccountResponse> accountResponses = savingsAccountList
            .stream()
            .map(savingsAccount -> new AccountResponse(
                savingsAccount.getId(),
                savingsAccount.getAccountNumber(),
                savingsAccount.getCustomerName(),
                savingsAccount.getAccountNickname()
                )
            )
            .toList();
        return new ResponseEntity<>(accountResponses, HttpStatus.OK);
    }

    /**
     * Endpoint to delete a savings bank account by its ID.
     *
     * @param id The unique ID of the account to delete.
     * @return ResponseEntity with the ID of the deleted account.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccountById(@PathVariable String id)
    {
        accountService.deleteAccountById(id);
        return new ResponseEntity<>(id, HttpStatus.OK); // Return the ID of the deleted account with 200 OK
    }
}

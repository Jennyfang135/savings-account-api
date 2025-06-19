package com.interview.save.account;

import com.interview.save.account.exception.DatabaseOperationException;
import com.interview.save.account.exception.ResourceNotFoundException;
import com.interview.save.account.model.SavingsAccount;
import com.interview.save.account.model.SavingsAccountCreateRequest;
import com.interview.save.account.repository.SavingsAccountRepository;
import com.interview.save.account.service.SavingsAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.Optional;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavingsAccountApplicationTests {

    @Mock
    private SavingsAccountRepository accountRepository; // Mock the repository

    @InjectMocks
    private SavingsAccountService accountService; // Inject mocks into the service

    private SavingsAccount testAccount;
    private SavingsAccountCreateRequest testRequest;
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^1\\d{9}$");

    @BeforeEach
    void setUp() {
        testAccount = new SavingsAccount("1234567890", "John Doe", "MySavings");
        testAccount.setId("some-uuid"); // Simulate ID generation for retrieved account

        testRequest = new SavingsAccountCreateRequest();
        testRequest.setCustomerName("John Doe");
        testRequest.setAccountNickname("MySavings");
    }

    @Test
    void testCreateAccount_Success() {
        // Mock behavior: countByCustomerName returns empty, findByAccountNumber returns empty, save returns the account
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty()); // Ensure account number is unique
        when(accountRepository.save(any(SavingsAccount.class))).thenReturn(testAccount);

        SavingsAccount createdAccount = accountService.createAccount(testRequest);

        assertNotNull(createdAccount);
        assertEquals(testRequest.getCustomerName(), createdAccount.getCustomerName());
        assertEquals(testRequest.getAccountNickname(), createdAccount.getAccountNickname());
        assertNotNull(createdAccount.getAccountNumber()); // Account number should be generated
        assertNotNull(createdAccount.getId()); // ID should be generated (though not by service directly)
        assertTrue(ACCOUNT_NUMBER_PATTERN.matcher(createdAccount.getAccountNumber()).matches(),
            "Generated account number should match pattern '1' followed by 9 digits");

        // Verify repository methods were called
        verify(accountRepository, times(1)).findByAccountNumber(anyString()); // Account number uniqueness check
        verify(accountRepository, times(1)).save(any(SavingsAccount.class));
    }

    @Test
    void testCreateAccount_DatabaseDown() {
        // Simulate database access failure during save operation
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());
        when(accountRepository.save(any(SavingsAccount.class))).thenThrow(new DataAccessResourceFailureException("DB connection lost"));

        // Expect DatabaseOperationException
        assertThrows(DatabaseOperationException.class, () -> accountService.createAccount(testRequest));

        verify(accountRepository, times(1)).save(any(SavingsAccount.class));
    }


    @Test
    void testGetAccount_Success() {
        when(accountRepository.findByAccountNumber(testAccount.getAccountNumber())).thenReturn(Optional.of(testAccount));

        SavingsAccount foundAccount = accountService.getAccount(testAccount.getAccountNumber());

        assertNotNull(foundAccount);
        assertEquals(testAccount.getAccountNumber(), foundAccount.getAccountNumber());
        assertEquals(testAccount.getCustomerName(), foundAccount.getCustomerName());
        assertEquals(testAccount.getAccountNickname(), foundAccount.getAccountNickname());

        verify(accountRepository, times(1)).findByAccountNumber(testAccount.getAccountNumber());
    }

    @Test
    void testGetAccount_NotFound() {
        // Mock behavior: findByAccountNumber returns empty optional
        when(accountRepository.findByAccountNumber("nonExistentAccount")).thenReturn(Optional.empty());

        // Expect ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccount("nonExistentAccount"));

        verify(accountRepository, times(1)).findByAccountNumber("nonExistentAccount");
    }

    @Test
    void testGetAccount_DatabaseDown() {
        // Simulate database access failure during find operation
        when(accountRepository.findByAccountNumber(anyString())).thenThrow(new DataAccessResourceFailureException("DB connection lost"));

        // Expect DatabaseOperationException
        assertThrows(DatabaseOperationException.class, () -> accountService.getAccount("anyAccount"));

        verify(accountRepository, times(1)).findByAccountNumber(anyString());
    }
}

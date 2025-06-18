package com.interview.save.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

@Entity(name = "accounts")
public class SavingsAccount
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String customerName;

    @Size(min = 5, max = 30, message = "Account nick name should be between 5 to 30 characters")
    private String accountNickName;

    public SavingsAccount() {
    }

    public SavingsAccount(final String accountNumber, final String customerName, final String accountNickname)
    {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.accountNickName = accountNickname;
    }

    // Getters and Setters
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }

    public String getAccountNickname()
    {
        return accountNickName;
    }

    public void setAccountNickname(String accountNickname)
    {
        this.accountNickName = accountNickname;
    }

    @Override
    public String toString()
    {
        return "Account{" +
            "id='" + id + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", customerName='" + customerName + '\'' +
            ", accountNickname='" + accountNickName + '\'' +
            '}';
    }

}
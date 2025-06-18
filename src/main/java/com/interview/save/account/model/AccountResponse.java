package com.interview.save.account.model;

public class AccountResponse
{
    private String id;
    private String accountNumber;
    private String customerName;
    private String accountNickname;

    // Constructor to convert Account entity to AccountResponse DTO
    public AccountResponse(final String id, final String accountNumber, final String customerName, final String accountNickname)
    {
        this.id = id;
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.accountNickname = accountNickname;
    }

    // Getters and Setters
    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(final String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(final String customerName)
    {
        this.customerName = customerName;
    }

    public String getAccountNickname()
    {
        return accountNickname;
    }

    public void setAccountNickname(final String accountNickname)
    {
        this.accountNickname = accountNickname;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
            "id='" + id + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", customerName='" + customerName + '\'' +
            ", accountNickname='" + accountNickname + '\'' +
            '}';
    }
}

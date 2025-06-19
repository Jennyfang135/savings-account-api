package com.interview.save.account.model;

import com.interview.save.account.validation.AccountLimit;
import com.interview.save.account.validation.OffensiveNickname;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SavingsAccountCreateRequest
{
    @NotBlank(message = "Customer name is mandatory")
    // Custom validation to check the 5-account limit.
    // Please NOTE 'customerId' might be a better option as different customers may have same customer names,
    // for now we assume that customerName will be unique in this project.
    @AccountLimit
    private String customerName;

    @Size(min = 5, max = 30, message = "Account nickname must be between 5 and 30 characters")
    @OffensiveNickname
    private String accountNickname;

    // Getters and Setters
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
    public String toString()
    {
        return "AccountCreateRequest{" +
            "customerName='" + customerName + '\'' +
            ", accountNickname='" + accountNickname + '\'' +
            '}';
    }
}

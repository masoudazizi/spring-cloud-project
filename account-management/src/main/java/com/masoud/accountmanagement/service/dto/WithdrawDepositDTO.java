package com.masoud.accountmanagement.service.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class WithdrawDepositDTO {
    @NotNull
    @NotEmpty
    private String accountNumber;
    @PositiveOrZero
    private BigDecimal amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "WithdrawDeposit{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}

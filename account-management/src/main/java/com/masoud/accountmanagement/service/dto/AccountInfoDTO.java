package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.AccountInfo;
import com.masoud.accountmanagement.domain.enumeration.AccountType;
import com.masoud.accountmanagement.service.validation.AccountNumberUniqueness;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link AccountInfo} entity.
 */
@AccountNumberUniqueness
public class AccountInfoDTO implements Serializable {
    
    private Long id;

    @PositiveOrZero
    private BigDecimal balance;

    @NotNull
    @NotEmpty
    private String accountNumber;

    private String description;

    @NotNull
    private AccountType accountType;

    @NotNull
    private Long fundId;

    @NotNull
    private Long customerId;

    @NotNull
    private Long bankBranchId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getFundId() {
        return fundId;
    }

    public void setFundId(Long fundId) {
        this.fundId = fundId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBankBranchId() {
        return bankBranchId;
    }

    public void setBankBranchId(Long bankBranchId) {
        this.bankBranchId = bankBranchId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountInfoDTO accountInfoDTO = (AccountInfoDTO) o;
        if (accountInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), accountInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AccountInfoDTO{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", accountNumber='" + getAccountNumber() + "'" +
            ", description='" + getDescription() + "'" +
            ", accountType='" + getAccountType() + "'" +
            ", fundId=" + getFundId() +
            ", customerId=" + getCustomerId() +
            ", bankBranchId=" + getBankBranchId() +
            "}";
    }
}

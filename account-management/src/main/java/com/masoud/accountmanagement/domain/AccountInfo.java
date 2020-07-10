package com.masoud.accountmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masoud.accountmanagement.domain.enumeration.AccountType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "account_info")
public class AccountInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "account_number", nullable = false , unique = true)
    private String accountNumber;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("accountInfos")
    private Fund fund;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("accountInfos")
    private Customer customer;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("accountInfos")
    private BankBranch bankBranch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AccountInfo balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountInfo accountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getDescription() {
        return description;
    }

    public AccountInfo description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public AccountInfo accountType(AccountType accountType) {
        this.accountType = accountType;
        return this;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Fund getFund() {
        return fund;
    }

    public AccountInfo fund(Fund fund) {
        this.fund = fund;
        return this;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public Customer getCustomer() {
        return customer;
    }

    public AccountInfo customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BankBranch getBankBranch() {
        return bankBranch;
    }

    public AccountInfo bankBranch(BankBranch bankBranch) {
        this.bankBranch = bankBranch;
        return this;
    }

    public void setBankBranch(BankBranch bankBranch) {
        this.bankBranch = bankBranch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountInfo)) {
            return false;
        }
        return id != null && id.equals(((AccountInfo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "id=" + getId() +
                ", balance=" + getBalance() +
                ", accountNumber='" + getAccountNumber() + "'" +
                ", description='" + getDescription() + "'" +
                ", accountType='" + getAccountType() + "'" +
                "}";
    }
}

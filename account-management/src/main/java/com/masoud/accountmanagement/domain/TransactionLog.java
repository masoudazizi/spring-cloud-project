package com.masoud.accountmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.masoud.accountmanagement.domain.enumeration.ActionType;
import com.masoud.accountmanagement.domain.enumeration.FundCode;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transaction_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransactionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time")
    private ZonedDateTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private ActionType actionType;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "status")
    private String status;

    @Column(name = "tracking_code")
    private String trackingCode;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_fund")
    private FundCode toFund;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_fund")
    private FundCode fromFund;

    @ManyToOne
    @JsonIgnoreProperties("transactionLogs")
    private AccountInfo accountInfo;

    @ManyToOne
    @JsonIgnoreProperties("transactionLogs")
    private AccountInfo fromAccount;

    @ManyToOne
    @JsonIgnoreProperties("transactionLogs")
    private AccountInfo toAccount;

    @ManyToOne
    @JsonIgnoreProperties("transactionLogs")
    private BankBranch branch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public TransactionLog time(ZonedDateTime time) {
        this.time = time;
        return this;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public TransactionLog actionType(ActionType actionType) {
        this.actionType = actionType;
        return this;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Long getBalance() {
        return balance;
    }

    public TransactionLog balance(Long balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public TransactionLog status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public TransactionLog description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public TransactionLog accountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        return this;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public AccountInfo getFromAccount() {
        return fromAccount;
    }

    public TransactionLog fromAccount(AccountInfo accountInfo) {
        this.fromAccount = accountInfo;
        return this;
    }

    public void setFromAccount(AccountInfo accountInfo) {
        this.fromAccount = accountInfo;
    }

    public AccountInfo getToAccount() {
        return toAccount;
    }

    public TransactionLog toAccount(AccountInfo accountInfo) {
        this.toAccount = accountInfo;
        return this;
    }

    public void setToAccount(AccountInfo accountInfo) {
        this.toAccount = accountInfo;
    }

    public FundCode getFromFund() {
        return fromFund;
    }

    public TransactionLog fromFund(FundCode fromFund) {
        this.fromFund = fromFund;
        return this;
    }

    public void setFromFund(FundCode fromFund) {
        this.fromFund = fromFund;
    }

    public FundCode getToFund() {
        return toFund;
    }

    public TransactionLog toFund(FundCode toFund) {
        this.toFund = toFund;
        return this;
    }

    public void setToFund(FundCode toFund) {
        this.toFund = toFund;
    }

    public BankBranch getBranch() {
        return branch;
    }

    public TransactionLog branch(BankBranch bankBranch) {
        this.branch = bankBranch;
        return this;
    }

    public void setBranch(BankBranch bankBranch) {
        this.branch = bankBranch;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public TransactionLog trackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionLog)) {
            return false;
        }
        return id != null && id.equals(((TransactionLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TransactionLog{" +
                "id=" + id +
                ", time=" + time +
                ", actionType=" + actionType +
                ", balance=" + balance +
                ", status='" + status + '\'' +
                ", description='" + description + '\'' +
                ", accountInfo=" + accountInfo +
                ", fromAccount=" + fromAccount +
                ", fromFund=" + fromFund +
                ", toAccount=" + toAccount +
                ", toFund=" + toFund +
                ", branch=" + branch +
                '}';
    }
}

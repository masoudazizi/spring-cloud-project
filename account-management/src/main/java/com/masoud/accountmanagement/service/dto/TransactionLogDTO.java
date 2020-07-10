package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.TransactionLog;
import com.masoud.accountmanagement.domain.enumeration.ActionType;
import com.masoud.accountmanagement.domain.enumeration.FundCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link TransactionLog} entity.
 */
public class TransactionLogDTO implements Serializable {

    private Long id;

    private ZonedDateTime time;

    private ActionType actionType;

    private BigDecimal balance;

    private String status;

    private String trackingCode;

    private String description;

    private Long fromAccountId;

    private Long toAccountId;

    private FundCode fromFund;

    private FundCode toFund;

    private Long branchId;

    public TransactionLogDTO() {
    }

    public TransactionLogDTO(ZonedDateTime time, ActionType actionType, BigDecimal balance
            , Long fromAccountId, FundCode fromFund, Long toAccountId, FundCode toFund, String trackingCode) {
        this.time = time;
        this.actionType = actionType;
        this.balance = balance;
        this.fromAccountId = fromAccountId;
        this.fromFund = fromFund;
        this.toAccountId = toAccountId;
        this.toFund = toFund;
        this.trackingCode = trackingCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public FundCode getFromFund() {
        return fromFund;
    }

    public void setFromFund(FundCode fromFund) {
        this.fromFund = fromFund;
    }

    public FundCode getToFund() {
        return toFund;
    }

    public void setToFund(FundCode toFund) {
        this.toFund = toFund;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long bankBranchId) {
        this.branchId = bankBranchId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionLogDTO transactionLogDTO = (TransactionLogDTO) o;
        if (transactionLogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionLogDTO{" +
                "id=" + id +
                ", time=" + time +
                ", actionType=" + actionType +
                ", balance=" + balance +
                ", status='" + status + '\'' +
                ", trackingCode='" + trackingCode + '\'' +
                ", description='" + description + '\'' +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", fromFund=" + fromFund +
                ", toFund=" + toFund +
                ", branchId=" + branchId +
                '}';
    }
}

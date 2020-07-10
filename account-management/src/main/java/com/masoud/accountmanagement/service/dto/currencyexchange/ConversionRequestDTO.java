package com.masoud.accountmanagement.service.dto.currencyexchange;

import com.masoud.accountmanagement.domain.enumeration.FundCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ConversionRequestDTO implements Serializable {

    @NotNull
    private FundCode from;

    @NotNull
    private FundCode to;

    @NotNull
    private BigDecimal amount;

    public ConversionRequestDTO() {
    }

    public ConversionRequestDTO(FundCode from, FundCode to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public FundCode getFrom() {
        return from;
    }

    public void setFrom(FundCode from) {
        this.from = from;
    }

    public FundCode getTo() {
        return to;
    }

    public void setTo(FundCode to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionRequestDTO that = (ConversionRequestDTO) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, amount);
    }

    @Override
    public String toString() {
        return "ConversionRequestDTO{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                '}';
    }
}

package com.masoud.currencyexchange.service.dto;

import com.masoud.currencyexchange.service.enumeration.Status;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConversionResponseDTO implements Serializable {

    private Status status;
    private BigDecimal conversionResult;
    private String errorCode;
    private String errorMessage;

    public ConversionResponseDTO() {
    }

    public ConversionResponseDTO(Status status, BigDecimal conversionResult, String errorCode, String errorMessage) {
        this.status = status;
        this.conversionResult = conversionResult;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getConversionResult() {
        return conversionResult;
    }

    public void setConversionResult(BigDecimal conversionResult) {
        this.conversionResult = conversionResult;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ConversionResponseDTO{" +
                "status=" + status +
                ", conversionResult=" + conversionResult +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

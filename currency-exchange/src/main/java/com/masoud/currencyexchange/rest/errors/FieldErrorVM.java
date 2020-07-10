package com.masoud.currencyexchange.rest.errors;

import java.io.Serializable;

public class FieldErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;

    private final String description;

    public FieldErrorVM(String dto, String field, String message, String description) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
        this.description = description;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}

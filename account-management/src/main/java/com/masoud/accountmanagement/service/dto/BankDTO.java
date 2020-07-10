package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.Bank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Bank} entity.
 */
public class BankDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BankDTO bankDTO = (BankDTO) o;
        if (bankDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bankDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BankDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}

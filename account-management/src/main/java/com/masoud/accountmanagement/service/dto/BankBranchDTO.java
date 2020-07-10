package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.BankBranch;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link BankBranch} entity.
 */
public class BankBranchDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    private String code;

    private String phone;

    @NotNull
    private Long bankId;
    
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BankBranchDTO bankBranchDTO = (BankBranchDTO) o;
        if (bankBranchDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bankBranchDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BankBranchDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", phone='" + getPhone() + "'" +
            ", bankId=" + getBankId() +
            "}";
    }
}

package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.Fund;
import com.masoud.accountmanagement.domain.enumeration.FundCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Fund} entity.
 */
public class FundDTO implements Serializable {
    
    private Long id;

    private String name;

    @NotNull
    private FundCode code;

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

    public FundCode getCode() {
        return code;
    }

    public void setCode(FundCode code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FundDTO fundDTO = (FundDTO) o;
        if (fundDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fundDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FundDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            "}";
    }
}

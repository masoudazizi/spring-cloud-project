package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.City;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link City} entity.
 */
public class CityDTO implements Serializable {
    
    private Long id;

    @NotNull
    @Size(max = 100)
    private String cityName;

    @NotNull
    private Long provinceId;

    private String provinceProvinceName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceProvinceName() {
        return provinceProvinceName;
    }

    public void setProvinceProvinceName(String provinceProvinceName) {
        this.provinceProvinceName = provinceProvinceName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CityDTO cityDTO = (CityDTO) o;
        if (cityDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cityDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CityDTO{" +
            "id=" + getId() +
            ", cityName='" + getCityName() + "'" +
            ", provinceId=" + getProvinceId() +
            ", provinceProvinceName='" + getProvinceProvinceName() + "'" +
            "}";
    }
}

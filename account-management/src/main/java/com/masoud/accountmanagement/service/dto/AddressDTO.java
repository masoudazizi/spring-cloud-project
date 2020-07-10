package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.Address;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Address} entity.
 */
public class AddressDTO implements Serializable {
    
    private Long id;

    @Size(max = 200)
    private String addressDetail;

    @NotNull
    @Size(max = 20)
    private String tell1;

    @Size(max = 20)
    private String tell2;

    @Size(min = 10, max = 10)
    private String postalCode;

    @NotNull
    private Long customerId;

    private Long provinceId;

    private String provinceProvinceName;

    private Long cityId;

    private String cityCityName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getTell1() {
        return tell1;
    }

    public void setTell1(String tell1) {
        this.tell1 = tell1;
    }

    public String getTell2() {
        return tell2;
    }

    public void setTell2(String tell2) {
        this.tell2 = tell2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityCityName() {
        return cityCityName;
    }

    public void setCityCityName(String cityCityName) {
        this.cityCityName = cityCityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (addressDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), addressDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", addressDetail='" + addressDetail + '\'' +
                ", tell1='" + tell1 + '\'' +
                ", tell2='" + tell2 + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", customerId=" + customerId +
                ", provinceId=" + provinceId +
                ", provinceProvinceName='" + provinceProvinceName + '\'' +
                ", cityId=" + cityId +
                ", cityCityName='" + cityCityName + '\'' +
                '}';
    }
}

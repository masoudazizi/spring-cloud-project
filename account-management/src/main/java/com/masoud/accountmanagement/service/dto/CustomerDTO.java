package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.domain.Customer;
import com.masoud.accountmanagement.domain.enumeration.Gender;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link Customer} entity.
 */
public class CustomerDTO implements Serializable {

    private Long id;

    @Size(max = 100)
    @NotNull
    private String name;

    @Size(max = 100)
    @NotNull
    private String family;

    private Gender gender;

    @Past
    private LocalDate birthDate;

    @NotNull
    @Size(max = 20)
    private String mobile;

    @Size(max = 100)
    private String email;

    @Lob
    private byte[] profilePic;

    private String profilePicContentType;

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

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicContentType() {
        return profilePicContentType;
    }

    public void setProfilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (customerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", family='" + getFamily() + "'" +
                ", gender='" + getGender() + "'" +
                ", birthdate='" + getBirthDate() + "'" +
                ", mobile='" + getMobile() + "'" +
                ", email='" + getEmail() + "'" +
                ", profilePic='" + getProfilePic() + "'" +
                "}";
    }
}

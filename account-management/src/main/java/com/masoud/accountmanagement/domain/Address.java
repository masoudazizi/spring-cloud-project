package com.masoud.accountmanagement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200)
    @Column(name = "address_detail", length = 200)
    private String addressDetail;

    @NotNull
    @Size(max = 20)
    @Column(name = "tell_1", length = 20, nullable = false)
    private String tell1;

    @Size(max = 20)
    @Column(name = "tell_2", length = 20)
    private String tell2;

    @Size(min = 10, max = 10)
    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("addresses")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("addresses")
    private Province province;

    @ManyToOne
    @JsonIgnoreProperties("addresses")
    private City city;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public Address addressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
        return this;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getTell1() {
        return tell1;
    }

    public Address tell1(String tell1) {
        this.tell1 = tell1;
        return this;
    }

    public void setTell1(String tell1) {
        this.tell1 = tell1;
    }

    public String getTell2() {
        return tell2;
    }

    public Address tell2(String tell2) {
        this.tell2 = tell2;
        return this;
    }

    public void setTell2(String tell2) {
        this.tell2 = tell2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Address postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address customer(Customer customer){
        this.customer = customer;
        return this;
    }

    public Province getProvince() {
        return province;
    }

    public Address province(Province province) {
        this.province = province;
        return this;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public City getCity() {
        return city;
    }

    public Address city(City city) {
        this.city = city;
        return this;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + getId() +
                ", addressDetail='" + getAddressDetail() + "'" +
                ", tell1='" + getTell1() + "'" +
                ", tell2='" + getTell2() + "'" +
                ", postalCode='" + getPostalCode() + "'" +
                "}";
    }
}

package com.masoud.accountmanagement.domain;

import com.masoud.accountmanagement.domain.enumeration.FundCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "fund")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fund implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false)
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

    public Fund name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FundCode getCode() {
        return code;
    }

    public Fund code(FundCode code) {
        this.code = code;
        return this;
    }

    public void setCode(FundCode code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fund)) {
            return false;
        }
        return id != null && id.equals(((Fund) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Fund{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", code='" + getCode() + "'" +
                "}";
    }
}

package com.masoud.accountmanagement.service.mapper;


import com.masoud.accountmanagement.service.dto.CustomerDTO;
import com.masoud.accountmanagement.domain.Customer;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {

    CustomerDTO toDto(Customer customer);

    Customer toEntity(CustomerDTO customerDTO);

    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}

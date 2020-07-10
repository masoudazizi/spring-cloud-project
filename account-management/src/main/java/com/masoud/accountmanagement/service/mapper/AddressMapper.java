package com.masoud.accountmanagement.service.mapper;


import com.masoud.accountmanagement.service.dto.AddressDTO;
import com.masoud.accountmanagement.domain.Address;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, ProvinceMapper.class, CityMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.provinceName", target = "provinceProvinceName")
    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.cityName", target = "cityCityName")
    AddressDTO toDto(Address address);

    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "provinceId", target = "province")
    @Mapping(source = "cityId", target = "city")
    Address toEntity(AddressDTO addressDTO);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}

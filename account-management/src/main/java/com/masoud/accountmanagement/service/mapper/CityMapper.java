package com.masoud.accountmanagement.service.mapper;


import com.masoud.accountmanagement.service.dto.CityDTO;
import com.masoud.accountmanagement.domain.City;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProvinceMapper.class})
public interface CityMapper extends EntityMapper<CityDTO, City> {

    @Mapping(source = "province.id", target = "provinceId")
    @Mapping(source = "province.provinceName", target = "provinceProvinceName")
    CityDTO toDto(City city);

    @Mapping(source = "provinceId", target = "province")
    City toEntity(CityDTO cityDTO);

    default City fromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }
}

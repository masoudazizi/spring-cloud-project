package com.masoud.accountmanagement.service.mapper;


import com.masoud.accountmanagement.service.dto.FundDTO;
import com.masoud.accountmanagement.domain.Fund;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fund} and its DTO {@link FundDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FundMapper extends EntityMapper<FundDTO, Fund> {



    default Fund fromId(Long id) {
        if (id == null) {
            return null;
        }
        Fund fund = new Fund();
        fund.setId(id);
        return fund;
    }
}

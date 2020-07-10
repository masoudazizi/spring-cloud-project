package com.masoud.accountmanagement.service.mapper;


import com.masoud.accountmanagement.service.dto.BankBranchDTO;
import com.masoud.accountmanagement.domain.BankBranch;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankBranch} and its DTO {@link BankBranchDTO}.
 */
@Mapper(componentModel = "spring", uses = {BankMapper.class})
public interface BankBranchMapper extends EntityMapper<BankBranchDTO, BankBranch> {

    @Mapping(source = "bank.id", target = "bankId")
    BankBranchDTO toDto(BankBranch bankBranch);

    @Mapping(source = "bankId", target = "bank")
    BankBranch toEntity(BankBranchDTO bankBranchDTO);

    default BankBranch fromId(Long id) {
        if (id == null) {
            return null;
        }
        BankBranch bankBranch = new BankBranch();
        bankBranch.setId(id);
        return bankBranch;
    }
}

package com.masoud.accountmanagement.service.mapper;


import com.masoud.accountmanagement.service.dto.AccountInfoDTO;
import com.masoud.accountmanagement.domain.AccountInfo;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountInfo} and its DTO {@link AccountInfoDTO}.
 */
@Mapper(componentModel = "spring", uses = {FundMapper.class, CustomerMapper.class, BankBranchMapper.class})
public interface AccountInfoMapper extends EntityMapper<AccountInfoDTO, AccountInfo> {

    @Mapping(source = "fund.id", target = "fundId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "bankBranch.id", target = "bankBranchId")
    AccountInfoDTO toDto(AccountInfo accountInfo);

    @Mapping(source = "fundId", target = "fund")
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "bankBranchId", target = "bankBranch")
    AccountInfo toEntity(AccountInfoDTO accountInfoDTO);

    default AccountInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setId(id);
        return accountInfo;
    }
}

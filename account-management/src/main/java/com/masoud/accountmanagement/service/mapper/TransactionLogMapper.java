package com.masoud.accountmanagement.service.mapper;


import com.masoud.accountmanagement.service.dto.TransactionLogDTO;
import com.masoud.accountmanagement.domain.TransactionLog;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransactionLog} and its DTO {@link TransactionLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {AccountInfoMapper.class, BankBranchMapper.class})
public interface TransactionLogMapper extends EntityMapper<TransactionLogDTO, TransactionLog> {

    @Mapping(source = "fromAccount.id", target = "fromAccountId")
    @Mapping(source = "toAccount.id", target = "toAccountId")
    @Mapping(source = "branch.id", target = "branchId")
    TransactionLogDTO toDto(TransactionLog transactionLog);

    @Mapping(source = "fromAccountId", target = "fromAccount")
    @Mapping(source = "toAccountId", target = "toAccount")
    @Mapping(source = "branchId", target = "branch")
    TransactionLog toEntity(TransactionLogDTO transactionLogDTO);

    default TransactionLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setId(id);
        return transactionLog;
    }
}

package com.masoud.accountmanagement.service;

import com.masoud.accountmanagement.domain.AccountInfo;
import com.masoud.accountmanagement.service.dto.AccountInfoDTO;
import com.masoud.accountmanagement.service.dto.TransferDTO;
import com.masoud.accountmanagement.service.dto.WithdrawDepositDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Interface for managing {@link AccountInfo}.
 */
public interface AccountInfoService {


    /**
     * Save a accountInfo.
     *
     * @param accountInfoDTO the entity to save.
     * @return the persisted entity.
     */
    AccountInfoDTO save(AccountInfoDTO accountInfoDTO);

    /**
     * Get all the accountInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountInfoDTO> findAll(Pageable pageable);

    /**
     * Get the "id" accountInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountInfoDTO> findOne(Long id);

    /**
     * Delete the "id" accountInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * find accountInfo by accountNumber
     *
     * @param accountNumber
     * @return
     */
    Optional<AccountInfoDTO> findByAccountNumber(String accountNumber);

    /**
     * withdraw source account number with amount
     *
     * @param withdrawDepositDTO
     */
    @Transactional
    String withdraw(WithdrawDepositDTO withdrawDepositDTO);

    /**
     * deposit source account number with amount
     *
     * @param withdrawDepositDTO
     */
    @Transactional
    String deposit(WithdrawDepositDTO withdrawDepositDTO);

    /**
     * transfer amount from source account number to destination account number
     *
     * @param transferDTO
     */
    @Transactional
    String transfer(TransferDTO transferDTO);
}

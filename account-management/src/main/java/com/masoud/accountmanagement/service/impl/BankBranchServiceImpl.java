package com.masoud.accountmanagement.service.impl;

import com.masoud.accountmanagement.service.BankBranchService;
import com.masoud.accountmanagement.service.dto.BankBranchDTO;
import com.masoud.accountmanagement.domain.BankBranch;
import com.masoud.accountmanagement.repository.BankBranchRepository;
import com.masoud.accountmanagement.service.mapper.BankBranchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BankBranch}.
 */
@Service
@Transactional
public class BankBranchServiceImpl implements BankBranchService {

    private final Logger log = LoggerFactory.getLogger(BankBranchServiceImpl.class);

    private final BankBranchRepository bankBranchRepository;

    private final BankBranchMapper bankBranchMapper;

    public BankBranchServiceImpl(BankBranchRepository bankBranchRepository, BankBranchMapper bankBranchMapper) {
        this.bankBranchRepository = bankBranchRepository;
        this.bankBranchMapper = bankBranchMapper;
    }

    /**
     * Save a bankBranch.
     *
     * @param bankBranchDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BankBranchDTO save(BankBranchDTO bankBranchDTO) {
        log.debug("Request to save BankBranch : {}", bankBranchDTO);
        BankBranch bankBranch = bankBranchMapper.toEntity(bankBranchDTO);
        bankBranch = bankBranchRepository.save(bankBranch);
        return bankBranchMapper.toDto(bankBranch);
    }

    /**
     * Get all the bankBranches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BankBranchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BankBranches");
        return bankBranchRepository.findAll(pageable)
                .map(bankBranchMapper::toDto);
    }

    /**
     * Get one bankBranch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BankBranchDTO> findOne(Long id) {
        log.debug("Request to get BankBranch : {}", id);
        return bankBranchRepository.findById(id)
                .map(bankBranchMapper::toDto);
    }

    /**
     * Delete the bankBranch by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BankBranch : {}", id);
        bankBranchRepository.deleteById(id);
    }
}

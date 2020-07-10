package com.masoud.accountmanagement.service;

import com.masoud.accountmanagement.domain.BankBranch;
import com.masoud.accountmanagement.service.dto.BankBranchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link BankBranch}.
 */
public interface BankBranchService {

    /**
     * Save a bankBranch.
     *
     * @param bankBranchDTO the entity to save.
     * @return the persisted entity.
     */
    BankBranchDTO save(BankBranchDTO bankBranchDTO);

    /**
     * Get all the bankBranches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BankBranchDTO> findAll(Pageable pageable);

    /**
     * Get the "id" bankBranch.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BankBranchDTO> findOne(Long id);

    /**
     * Delete the "id" bankBranch.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

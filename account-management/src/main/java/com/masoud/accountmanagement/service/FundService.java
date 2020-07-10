package com.masoud.accountmanagement.service;

import com.masoud.accountmanagement.domain.Fund;
import com.masoud.accountmanagement.service.dto.FundDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Fund}.
 */
public interface FundService {

    /**
     * Save a fund.
     *
     * @param fundDTO the entity to save.
     * @return the persisted entity.
     */
    FundDTO save(FundDTO fundDTO);

    /**
     * Get all the funds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FundDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fund.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FundDTO> findOne(Long id);

    /**
     * Delete the "id" fund.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

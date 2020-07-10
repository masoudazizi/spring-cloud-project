package com.masoud.accountmanagement.service;

import com.masoud.accountmanagement.domain.TransactionLog;
import com.masoud.accountmanagement.service.dto.TransactionLogDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Interface for managing {@link TransactionLog}.
 */
public interface TransactionLogService {

    /**
     * Save a transactionLog.
     *
     * @param transactionLogDTO the entity to save.
     * @return the persisted entity.
     */
    TransactionLogDTO save(TransactionLogDTO transactionLogDTO);

    /**
     * Get all the transactionLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransactionLogDTO> findAll(Pageable pageable);

    /**
     * Get all the transactionLogs by search between date.
     *
     * @param pageable  the pagination information.
     * @param startDate the start date
     * @param endDate   the end date
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    Page<TransactionLogDTO> findAllBetweenDate(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable);

    /**
     * Get the "id" transactionLog.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransactionLogDTO> findOne(Long id);

    /**
     * Get one transactionLog by trackingCode.
     *
     * @param trackingCode the trackingCode of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    Optional<TransactionLogDTO> findByTrackingCode(String trackingCode);

    /**
     * Delete the "id" transactionLog.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

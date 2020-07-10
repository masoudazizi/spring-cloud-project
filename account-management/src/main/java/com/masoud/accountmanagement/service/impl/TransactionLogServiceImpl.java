package com.masoud.accountmanagement.service.impl;

import com.masoud.accountmanagement.service.TransactionLogService;
import com.masoud.accountmanagement.service.dto.TransactionLogDTO;
import com.masoud.accountmanagement.domain.TransactionLog;
import com.masoud.accountmanagement.repository.TransactionLogRepository;
import com.masoud.accountmanagement.service.mapper.TransactionLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing {@link TransactionLog}.
 */
@Service
@Transactional
public class TransactionLogServiceImpl implements TransactionLogService {

    private final Logger log = LoggerFactory.getLogger(TransactionLogServiceImpl.class);

    private final TransactionLogRepository transactionLogRepository;

    private final TransactionLogMapper transactionLogMapper;

    public TransactionLogServiceImpl(TransactionLogRepository transactionLogRepository, TransactionLogMapper transactionLogMapper) {
        this.transactionLogRepository = transactionLogRepository;
        this.transactionLogMapper = transactionLogMapper;
    }

    /**
     * Save a transactionLog.
     *
     * @param transactionLogDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TransactionLogDTO save(TransactionLogDTO transactionLogDTO) {
        log.debug("Request to save TransactionLog : {}", transactionLogDTO);
        TransactionLog transactionLog = transactionLogMapper.toEntity(transactionLogDTO);
        transactionLog = transactionLogRepository.save(transactionLog);
        return transactionLogMapper.toDto(transactionLog);
    }

    /**
     * Get all the transactionLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionLogDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TransactionLogs");
        return transactionLogRepository.findAll(pageable)
                .map(transactionLogMapper::toDto);
    }

    /**
     * Get all the transactionLogs by search between date.
     *
     * @param pageable  the pagination information.
     * @param startDate the start date
     * @param endDate   the end date
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TransactionLogDTO> findAllBetweenDate(ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable) {
        log.debug("Request to get all TransactionLogs between start date {}, end date {}", startDate, endDate);
        return transactionLogRepository.findAllByTimeBetween(startDate, endDate, pageable)
                .map(transactionLogMapper::toDto);
    }

    /**
     * Get one transactionLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionLogDTO> findOne(Long id) {
        log.debug("Request to get TransactionLog : {}", id);
        return transactionLogRepository.findById(id)
                .map(transactionLogMapper::toDto);
    }

    /**
     * Get one transactionLog by trackingCode.
     *
     * @param trackingCode the trackingCode of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TransactionLogDTO> findByTrackingCode(String trackingCode) {
        log.debug("Request to get TransactionLog by trackingCode : {}", trackingCode);
        return transactionLogRepository.findByTrackingCode(trackingCode)
                .map(transactionLogMapper::toDto);
    }

    /**
     * Delete the transactionLog by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TransactionLog : {}", id);
        transactionLogRepository.deleteById(id);
    }
}

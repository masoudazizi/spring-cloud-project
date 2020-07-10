package com.masoud.accountmanagement.service.impl;

import com.masoud.accountmanagement.service.FundService;
import com.masoud.accountmanagement.service.dto.FundDTO;
import com.masoud.accountmanagement.domain.Fund;
import com.masoud.accountmanagement.repository.FundRepository;
import com.masoud.accountmanagement.service.mapper.FundMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Fund}.
 */
@Service
@Transactional
public class FundServiceImpl implements FundService {

    private final Logger log = LoggerFactory.getLogger(FundServiceImpl.class);

    private final FundRepository fundRepository;

    private final FundMapper fundMapper;

    public FundServiceImpl(FundRepository fundRepository, FundMapper fundMapper) {
        this.fundRepository = fundRepository;
        this.fundMapper = fundMapper;
    }

    /**
     * Save a fund.
     *
     * @param fundDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FundDTO save(FundDTO fundDTO) {
        log.debug("Request to save Fund : {}", fundDTO);
        Fund fund = fundMapper.toEntity(fundDTO);
        fund = fundRepository.save(fund);
        return fundMapper.toDto(fund);
    }

    /**
     * Get all the funds.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<FundDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Funds");
        return fundRepository.findAll(pageable)
                .map(fundMapper::toDto);
    }

    /**
     * Get one fund by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FundDTO> findOne(Long id) {
        log.debug("Request to get Fund : {}", id);
        return fundRepository.findById(id)
                .map(fundMapper::toDto);
    }

    /**
     * Delete the fund by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fund : {}", id);
        fundRepository.deleteById(id);
    }
}

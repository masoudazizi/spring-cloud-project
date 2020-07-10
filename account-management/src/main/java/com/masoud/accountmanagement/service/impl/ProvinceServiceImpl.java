package com.masoud.accountmanagement.service.impl;

import com.masoud.accountmanagement.service.ProvinceService;
import com.masoud.accountmanagement.service.dto.ProvinceDTO;
import com.masoud.accountmanagement.domain.Province;
import com.masoud.accountmanagement.repository.ProvinceRepository;
import com.masoud.accountmanagement.service.mapper.ProvinceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Province}.
 */
@Service
@Transactional
public class ProvinceServiceImpl implements ProvinceService {

    private final Logger log = LoggerFactory.getLogger(ProvinceServiceImpl.class);

    private final ProvinceRepository provinceRepository;

    private final ProvinceMapper provinceMapper;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceMapper provinceMapper) {
        this.provinceRepository = provinceRepository;
        this.provinceMapper = provinceMapper;
    }

    /**
     * Save a province.
     *
     * @param provinceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ProvinceDTO save(ProvinceDTO provinceDTO) {
        log.debug("Request to save Province : {}", provinceDTO);
        Province province = provinceMapper.toEntity(provinceDTO);
        province = provinceRepository.save(province);
        return provinceMapper.toDto(province);
    }

    /**
     * Get all the provinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProvinceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Provinces");
        return provinceRepository.findAll(pageable)
                .map(provinceMapper::toDto);
    }

    /**
     * Get one province by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProvinceDTO> findOne(Long id) {
        log.debug("Request to get Province : {}", id);
        return provinceRepository.findById(id)
                .map(provinceMapper::toDto);
    }

    /**
     * Delete the province by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Province : {}", id);
        provinceRepository.deleteById(id);
    }
}

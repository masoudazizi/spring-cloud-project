package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.domain.Fund;
import com.masoud.accountmanagement.service.FundService;
import com.masoud.accountmanagement.service.dto.FundDTO;
import com.masoud.accountmanagement.rest.errors.BadRequestAlertException;
import com.masoud.accountmanagement.rest.util.HeaderUtil;
import com.masoud.accountmanagement.rest.util.PaginationUtil;
import com.masoud.accountmanagement.rest.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Fund}.
 */
@RestController
@RequestMapping("/api")
public class FundResource {

    private final Logger log = LoggerFactory.getLogger(FundResource.class);

    private static final String ENTITY_NAME = "fund";

    @Value("${spring.application.name}")
    private String applicationName;

    private final FundService fundService;

    public FundResource(FundService fundService) {
        this.fundService = fundService;
    }

    /**
     * {@code POST  /funds} : Create a new fund.
     *
     * @param fundDTO the fundDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fundDTO, or with status {@code 400 (Bad Request)} if the fund has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/funds")
    public ResponseEntity<FundDTO> createFund(@Valid @RequestBody FundDTO fundDTO) throws URISyntaxException {
        log.debug("REST request to save Fund : {}", fundDTO);
        if (fundDTO.getId() != null) {
            throw new BadRequestAlertException("A new fund cannot already have an ID", ENTITY_NAME, "id.exists");
        }
        FundDTO result = fundService.save(fundDTO);
        return ResponseEntity.created(new URI("/api/funds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /funds} : Updates an existing fund.
     *
     * @param fundDTO the fundDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fundDTO,
     * or with status {@code 400 (Bad Request)} if the fundDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fundDTO couldn't be updated.
     */
    @PutMapping("/funds")
    public ResponseEntity<FundDTO> updateFund(@Valid @RequestBody FundDTO fundDTO) {
        log.debug("REST request to update Fund : {}", fundDTO);
        if (fundDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id.null");
        }
        FundDTO result = fundService.save(fundDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fundDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /funds} : get all the funds.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of funds in body.
     */
    @GetMapping("/funds")
    public ResponseEntity<List<FundDTO>> getAllFunds(Pageable pageable) {
        log.debug("REST request to get a page of Funds");
        Page<FundDTO> page = fundService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /funds/:id} : get the "id" fund.
     *
     * @param id the id of the fundDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fundDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/funds/{id}")
    public ResponseEntity<FundDTO> getFund(@PathVariable Long id) {
        log.debug("REST request to get Fund : {}", id);
        Optional<FundDTO> fundDTO = fundService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fundDTO);
    }

    /**
     * {@code DELETE  /funds/:id} : delete the "id" fund.
     *
     * @param id the id of the fundDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/funds/{id}")
    public ResponseEntity<Void> deleteFund(@PathVariable Long id) {
        log.debug("REST request to delete Fund : {}", id);
        fundService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

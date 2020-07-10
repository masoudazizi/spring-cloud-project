package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.domain.BankBranch;
import com.masoud.accountmanagement.rest.util.ResponseUtil;
import com.masoud.accountmanagement.service.BankBranchService;
import com.masoud.accountmanagement.service.dto.BankBranchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.masoud.accountmanagement.rest.errors.BadRequestAlertException;
import com.masoud.accountmanagement.rest.util.HeaderUtil;
import com.masoud.accountmanagement.rest.util.PaginationUtil;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link BankBranch}.
 */
@RestController
@RequestMapping("/api")
public class BankBranchResource {

    private final Logger log = LoggerFactory.getLogger(BankBranchResource.class);

    private static final String ENTITY_NAME = "bankBranch";

    @Value("${spring.application.name}")
    private String applicationName;

    private final BankBranchService bankBranchService;

    public BankBranchResource(BankBranchService bankBranchService) {
        this.bankBranchService = bankBranchService;
    }

    /**
     * {@code POST  /bank-branches} : Create a new bankBranch.
     *
     * @param bankBranchDTO the bankBranchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankBranchDTO, or with status {@code 400 (Bad Request)} if the bankBranch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bank-branches")
    public ResponseEntity<BankBranchDTO> createBankBranch(@Valid @RequestBody BankBranchDTO bankBranchDTO) throws URISyntaxException {
        log.debug("REST request to save BankBranch : {}", bankBranchDTO);
        if (bankBranchDTO.getId() != null) {
            throw new BadRequestAlertException("A new bankBranch cannot already have an ID", ENTITY_NAME, "id.exists");
        }
        BankBranchDTO result = bankBranchService.save(bankBranchDTO);
        return ResponseEntity.created(new URI("/api/bank-branches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bank-branches} : Updates an existing bankBranch.
     *
     * @param bankBranchDTO the bankBranchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankBranchDTO,
     * or with status {@code 400 (Bad Request)} if the bankBranchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankBranchDTO couldn't be updated.
     */
    @PutMapping("/bank-branches")
    public ResponseEntity<BankBranchDTO> updateBankBranch(@Valid @RequestBody BankBranchDTO bankBranchDTO) {
        log.debug("REST request to update BankBranch : {}", bankBranchDTO);
        if (bankBranchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id.null");
        }
        BankBranchDTO result = bankBranchService.save(bankBranchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankBranchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bank-branches} : get all the bankBranches.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankBranches in body.
     */
    @GetMapping("/bank-branches")
    public ResponseEntity<List<BankBranchDTO>> getAllBankBranches(Pageable pageable) {
        log.debug("REST request to get a page of BankBranches");
        Page<BankBranchDTO> page = bankBranchService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-branches/:id} : get the "id" bankBranch.
     *
     * @param id the id of the bankBranchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankBranchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bank-branches/{id}")
    public ResponseEntity<BankBranchDTO> getBankBranch(@PathVariable Long id) {
        log.debug("REST request to get BankBranch : {}", id);
        Optional<BankBranchDTO> bankBranchDTO = bankBranchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankBranchDTO);
    }

    /**
     * {@code DELETE  /bank-branches/:id} : delete the "id" bankBranch.
     *
     * @param id the id of the bankBranchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bank-branches/{id}")
    public ResponseEntity<Void> deleteBankBranch(@PathVariable Long id) {
        log.debug("REST request to delete BankBranch : {}", id);
        bankBranchService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

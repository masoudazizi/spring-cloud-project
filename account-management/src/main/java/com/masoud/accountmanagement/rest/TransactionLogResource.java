package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.domain.TransactionLog;
import com.masoud.accountmanagement.rest.util.ResponseUtil;
import com.masoud.accountmanagement.service.TransactionLogService;
import com.masoud.accountmanagement.service.dto.TransactionLogDTO;
import com.masoud.accountmanagement.rest.errors.BadRequestAlertException;
import com.masoud.accountmanagement.rest.util.HeaderUtil;
import com.masoud.accountmanagement.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link TransactionLog}.
 */
@RestController
@RequestMapping("/api")
public class TransactionLogResource {

    private final Logger log = LoggerFactory.getLogger(TransactionLogResource.class);

    private static final String ENTITY_NAME = "transactionLog";

    @Value("${spring.application.name}")
    private String applicationName;

    private final TransactionLogService transactionLogService;

    public TransactionLogResource(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
    }

    /**
     * {@code POST  /transaction-logs} : Create a new transactionLog.
     *
     * @param transactionLogDTO the transactionLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionLogDTO, or with status {@code 400 (Bad Request)} if the transactionLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-logs")
    public ResponseEntity<TransactionLogDTO> createTransactionLog(@RequestBody TransactionLogDTO transactionLogDTO) throws URISyntaxException {
        log.debug("REST request to save TransactionLog : {}", transactionLogDTO);
        if (transactionLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionLog cannot already have an ID", ENTITY_NAME, "id.exists");
        }
        TransactionLogDTO result = transactionLogService.save(transactionLogDTO);
        return ResponseEntity.created(new URI("/api/transaction-logs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /transaction-logs} : get all the transactionLogs between start date and end date.
     *
     * @param pageable  the pagination information.
     * @param startDate the start date
     * @param endDate   the end date
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionLogs in body.
     */
    @GetMapping("/transaction-logs/{startDate}/{endDate}")
    public ResponseEntity<List<TransactionLogDTO>> getAllTransactionLogs(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime startDate
            , @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime endDate, Pageable pageable) {
        log.debug("REST request to get a page of TransactionLogs between start-date : {} and end-date : {}", startDate, endDate);
        Page<TransactionLogDTO> page = transactionLogService.findAllBetweenDate(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-logs/:id} : get the "id" transactionLog.
     *
     * @param id the id of the transactionLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-logs/{id}")
    public ResponseEntity<TransactionLogDTO> getTransactionLog(@PathVariable Long id) {
        log.debug("REST request to get TransactionLog : {}", id);
        Optional<TransactionLogDTO> transactionLogDTO = transactionLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionLogDTO);
    }

    /**
     * {@code GET  /transaction-logs/tracking-code/:id} : get the "trackingCode" transactionLog.
     *
     * @param trackingCode the trackingCode of the transactionLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-logs/tracking-code/{trackingCode}")
    public ResponseEntity<TransactionLogDTO> getTransactionLog(@PathVariable String trackingCode) {
        log.debug("REST request to get TransactionLog by trackingCode : {}", trackingCode);
        Optional<TransactionLogDTO> transactionLogDTO = transactionLogService.findByTrackingCode(trackingCode);
        return ResponseUtil.wrapOrNotFound(transactionLogDTO);
    }
}

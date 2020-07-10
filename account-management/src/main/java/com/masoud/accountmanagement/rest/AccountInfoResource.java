package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.domain.AccountInfo;
import com.masoud.accountmanagement.rest.util.ResponseUtil;
import com.masoud.accountmanagement.service.AccountInfoService;
import com.masoud.accountmanagement.service.dto.AccountInfoDTO;
import com.masoud.accountmanagement.service.dto.TransferDTO;
import com.masoud.accountmanagement.service.dto.WithdrawDepositDTO;
import com.masoud.accountmanagement.rest.errors.BadRequestAlertException;
import com.masoud.accountmanagement.rest.util.HeaderUtil;
import com.masoud.accountmanagement.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing {@link AccountInfo}.
 */
@RestController
@RequestMapping("/api")
public class AccountInfoResource {

    private final Logger log = LoggerFactory.getLogger(AccountInfoResource.class);

    private static final String ENTITY_NAME = "accountInfo";

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * {@code POST  /account-infos} : Create a new accountInfo.
     *
     * @param accountInfoDTO the accountInfoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountInfoDTO, or with status {@code 400 (Bad Request)} if the accountInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-infos")
    public ResponseEntity<AccountInfoDTO> createAccountInfo(@Valid @RequestBody AccountInfoDTO accountInfoDTO) throws URISyntaxException {
        log.debug("REST request to save AccountInfo : {}", accountInfoDTO);
        if (accountInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountInfo cannot already have an ID", ENTITY_NAME, "id.exists");
        }
        AccountInfoDTO result = accountInfoService.save(accountInfoDTO);
        return ResponseEntity.created(new URI("/api/account-infos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /account-infos} : Updates an existing accountInfo.
     *
     * @param accountInfoDTO the accountInfoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountInfoDTO,
     * or with status {@code 400 (Bad Request)} if the accountInfoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountInfoDTO couldn't be updated.
     */
    @PutMapping("/account-infos")
    public ResponseEntity<AccountInfoDTO> updateAccountInfo(@Valid @RequestBody AccountInfoDTO accountInfoDTO) {
        log.debug("REST request to update AccountInfo : {}", accountInfoDTO);
        if (accountInfoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "id.null");
        }
        AccountInfoDTO result = accountInfoService.save(accountInfoDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountInfoDTO.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /account-infos} : get all the accountInfos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountInfos in body.
     */
    @GetMapping("/account-infos")
    public ResponseEntity<List<AccountInfoDTO>> getAllAccountInfos(Pageable pageable) {
        log.debug("REST request to get a page of AccountInfos");
        Page<AccountInfoDTO> page = accountInfoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-infos/:id} : get the "id" accountInfo.
     *
     * @param id the id of the accountInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-infos/{id}")
    public ResponseEntity<AccountInfoDTO> getAccountInfo(@PathVariable Long id) {
        log.debug("REST request to get AccountInfo : {}", id);
        Optional<AccountInfoDTO> accountInfoDTO = accountInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountInfoDTO);
    }

    /**
     * {@code DELETE  /account-infos/:id} : delete the "id" accountInfo.
     *
     * @param id the id of the accountInfoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-infos/{id}")
    public ResponseEntity<Void> deleteAccountInfo(@PathVariable Long id) {
        log.debug("REST request to delete AccountInfo : {}", id);
        accountInfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


    /**
     * {@code GET  /account-infos/account-number/:accountNumber} : get by "accountNumber" accountInfo.
     *
     * @param accountNumber the accountNumber of the accountInfoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountInfoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-infos/account-number/{accountNumber}")
    public ResponseEntity<AccountInfoDTO> getByAccountInfo(@PathVariable String accountNumber) {
        log.debug("REST request to get AccountInfo by accountNumber : {}", accountNumber);
        Optional<AccountInfoDTO> accountInfoDTO = accountInfoService.findByAccountNumber(accountNumber);
        return ResponseUtil.wrapOrNotFound(accountInfoDTO);
    }

    /**
     * {@code POST  /withdraw} : withdraw an specific amount from source account.
     *
     * @param withdrawDepositDTO to transfer
     * @return the {@link ResponseEntity} with status {@code 200(withdraw)} and with body the withdrawDeposit, or with status {@code 400 (Bad Request)}.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@Valid @RequestBody WithdrawDepositDTO withdrawDepositDTO) throws URISyntaxException {
        log.debug("REST request to withdraw withdrawDeposit : {}", withdrawDepositDTO);
        String transactionTrackingCode = accountInfoService.withdraw(withdrawDepositDTO);
        return ResponseEntity.created(new URI("/api/transaction-logs/tracking-code/" + transactionTrackingCode))
                .body(transactionTrackingCode);
    }

    /**
     * {@code POST  /deposit} : deposit an specific amount to destination account.
     *
     * @param withdrawDepositDTO to deposit
     * @return the {@link ResponseEntity} with status {@code 200(deposit)} and with body the withdrawDeposit, or with status {@code 400 (Bad Request)}.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody WithdrawDepositDTO withdrawDepositDTO) throws URISyntaxException {
        log.debug("REST request to deposit withdrawDeposit : {}", withdrawDepositDTO);
        String transactionTrackingCode = accountInfoService.deposit(withdrawDepositDTO);
        return ResponseEntity.created(new URI("/api/transaction-logs/tracking-code/" + transactionTrackingCode))
                .body(transactionTrackingCode);
    }


    /**
     * {@code POST  /transfer} : transfer an specific amount from source account to destination account.
     *
     * @param transferDTO to transfer
     * @return the {@link ResponseEntity} with status {@code 200(transfer)} and with body the transferDTO, or with status {@code 400 (Bad Request)}.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferDTO transferDTO) throws URISyntaxException {
        log.debug("REST request to transfer transferDTO : {}", transferDTO);
        String transactionTrackingCode = accountInfoService.transfer(transferDTO);
        return ResponseEntity.created(new URI("/api/transaction-logs/tracking-code/" + transactionTrackingCode))
                .body(transactionTrackingCode);
    }

}

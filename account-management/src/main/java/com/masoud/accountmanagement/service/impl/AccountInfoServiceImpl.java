package com.masoud.accountmanagement.service.impl;

import com.masoud.accountmanagement.domain.enumeration.ActionType;
import com.masoud.accountmanagement.repository.AccountInfoRepository;
import com.masoud.accountmanagement.service.AccountInfoService;
import com.masoud.accountmanagement.service.TransactionLogService;
import com.masoud.accountmanagement.service.dto.AccountInfoDTO;
import com.masoud.accountmanagement.service.dto.TransactionLogDTO;
import com.masoud.accountmanagement.service.dto.TransferDTO;
import com.masoud.accountmanagement.service.dto.WithdrawDepositDTO;
import com.masoud.accountmanagement.service.dto.currencyexchange.ConversionRequestDTO;
import com.masoud.accountmanagement.service.dto.currencyexchange.ConversionResponseDTO;
import com.masoud.accountmanagement.service.enumeration.Status;
import com.masoud.accountmanagement.service.mapper.AccountInfoMapper;
import com.masoud.accountmanagement.service.proxy.CurrencyExchangeProxy;
import com.masoud.accountmanagement.domain.AccountInfo;
import com.masoud.accountmanagement.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link AccountInfo}.
 */
@Service
@Transactional
public class AccountInfoServiceImpl implements AccountInfoService {

    private final Logger log = LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    private static final String ENTITY_NAME = "accountInfo";

    private final AccountInfoRepository accountInfoRepository;

    private final AccountInfoMapper accountInfoMapper;

    private final TransactionLogService transactionLogService;

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;

    public AccountInfoServiceImpl(AccountInfoRepository accountInfoRepository, AccountInfoMapper accountInfoMapper, TransactionLogService transactionLogService) {
        this.accountInfoRepository = accountInfoRepository;
        this.accountInfoMapper = accountInfoMapper;
        this.transactionLogService = transactionLogService;
    }

    /**
     * find accountInfo by accountNumber
     *
     * @param accountNumber to fid
     * @return AccountInfoDTO
     */
    @Override
    public Optional<AccountInfoDTO> findByAccountNumber(String accountNumber) {
        return accountInfoRepository.findByAccountNumber(accountNumber)
                .map(accountInfoMapper::toDto);
    }

    /**
     * Save a accountInfo.
     *
     * @param accountInfoDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AccountInfoDTO save(AccountInfoDTO accountInfoDTO) {
        log.debug("Request to save AccountInfo : {}", accountInfoDTO);
        AccountInfo accountInfo = accountInfoMapper.toEntity(accountInfoDTO);
        accountInfo = accountInfoRepository.save(accountInfo);
        return accountInfoMapper.toDto(accountInfo);
    }

    /**
     * Get all the accountInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AccountInfoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AccountInfos");
        return accountInfoRepository.findAll(pageable)
                .map(accountInfoMapper::toDto);
    }

    /**
     * Get one accountInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AccountInfoDTO> findOne(Long id) {
        log.debug("Request to get AccountInfo : {}", id);
        return accountInfoRepository.findById(id)
                .map(accountInfoMapper::toDto);
    }

    /**
     * Delete the accountInfo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccountInfo : {}", id);
        accountInfoRepository.deleteById(id);
    }


    /**
     * withdraw source account number with an specific amount
     *
     * @param withdrawDepositDTO
     */
    @Override
    public String withdraw(WithdrawDepositDTO withdrawDepositDTO) {
        log.debug("Request to withdraw  WithdrawDeposit : {}", withdrawDepositDTO);
        Optional<AccountInfo> accountInfoOptional = accountInfoRepository.findByAccountNumber(withdrawDepositDTO.getAccountNumber());
        if (!accountInfoOptional.isPresent()) {
            throw new BadRequestAlertException("account number not found", ENTITY_NAME, "account.number.not.found");
        }
        if (accountInfoOptional.get().getBalance().compareTo(withdrawDepositDTO.getAmount()) < 0) {
            throw new BadRequestAlertException("insufficient balance", ENTITY_NAME, "insufficient.balance");
        }
        BigDecimal originBalance = accountInfoOptional.get().getBalance();
        AccountInfo accountInfo = accountInfoOptional.get();
        accountInfo.setBalance(originBalance.subtract(withdrawDepositDTO.getAmount()));
        accountInfoRepository.save(accountInfo);

        // save transaction log
        TransactionLogDTO transactionLogDTO = new TransactionLogDTO(ZonedDateTime.now(ZoneId.systemDefault()), ActionType.WITHDRAW,
                withdrawDepositDTO.getAmount(), accountInfoOptional.get().getId(), accountInfoOptional.get().getFund().getCode(), null, null, generateTrackingCode());
        TransactionLogDTO transactionLogResult = transactionLogService.save(transactionLogDTO);
        return transactionLogResult.getTrackingCode();
    }

    /**
     * deposit source account number with amount
     *
     * @param withdrawDepositDTO
     */
    @Override
    public String deposit(WithdrawDepositDTO withdrawDepositDTO) {
        log.debug("Request to deposit  WithdrawDeposit : {}", withdrawDepositDTO);
        Optional<AccountInfo> accountInfoOptional = accountInfoRepository.findByAccountNumber(withdrawDepositDTO.getAccountNumber());
        if (!accountInfoOptional.isPresent()) {
            throw new BadRequestAlertException("account number not found", ENTITY_NAME, "account.number.not.found");
        }
        BigDecimal originBalance = accountInfoOptional.get().getBalance();
        AccountInfo accountInfo = accountInfoOptional.get();
        accountInfo.setBalance(originBalance.add(withdrawDepositDTO.getAmount()));
        accountInfoRepository.save(accountInfo);

        // save transaction log
        TransactionLogDTO transactionLogDTO = new TransactionLogDTO(ZonedDateTime.now(ZoneId.systemDefault()), ActionType.DEPOSIT,
                withdrawDepositDTO.getAmount(), null, null, accountInfoOptional.get().getId(), accountInfoOptional.get().getFund().getCode(), generateTrackingCode());
        TransactionLogDTO transactionLogResult = transactionLogService.save(transactionLogDTO);
        return transactionLogResult.getTrackingCode();
    }

    /**
     * transfer amount from source account number to destination account number
     *
     * @param transferDTO
     */
    @Override
    public String transfer(TransferDTO transferDTO) {
        log.debug("Request to transfer transferDTO : {}", transferDTO);
        if (transferDTO.getDestinationAccountNumber().equals(transferDTO.getSourceAccountNumber())) {
            throw new BadRequestAlertException("source and destination account number are the same ", ENTITY_NAME, "source.destination.are.the.same");
        }
        Optional<AccountInfo> sourceAccountInfoOptional = accountInfoRepository.findByAccountNumber(transferDTO.getSourceAccountNumber());
        if (!sourceAccountInfoOptional.isPresent()) {
            throw new BadRequestAlertException("source account number not found", ENTITY_NAME, "source.account.number.not.found");
        }
        if (sourceAccountInfoOptional.get().getBalance().compareTo(transferDTO.getAmount()) < 0) {
            throw new BadRequestAlertException("Insufficient source balance", ENTITY_NAME, "insufficient.source.balance");
        }
        Optional<AccountInfo> destinationAccountInfoOptional = accountInfoRepository.findByAccountNumber(transferDTO.getDestinationAccountNumber());
        if (!destinationAccountInfoOptional.isPresent()) {
            throw new BadRequestAlertException("destination account number not found", ENTITY_NAME, "destination.account.number.not.found");
        }
        if (destinationAccountInfoOptional.get().getId().equals(sourceAccountInfoOptional.get().getId())) {
            throw new BadRequestAlertException("source account is equal with destination account", ENTITY_NAME, "destination.source.account.is.same");
        }

        BigDecimal convertedBalance;
        // convert amount according to destination fund
        if (!destinationAccountInfoOptional.get().getFund().getId().equals(sourceAccountInfoOptional.get().getFund().getId())) {
            ConversionRequestDTO conversionRequestDTO = new ConversionRequestDTO(sourceAccountInfoOptional.get().getFund().getCode(), destinationAccountInfoOptional.get().getFund().getCode(), transferDTO.getAmount());
            ConversionResponseDTO conversionResponseDTO = currencyExchangeProxy.currencyConversion(conversionRequestDTO).getBody();
            // handle exception with fallback value
            if (conversionResponseDTO.getStatus().equals(Status.UNSUCCESSFUL)) {
                throw new BadRequestAlertException(conversionResponseDTO.getErrorMessage(), ENTITY_NAME, conversionResponseDTO.getErrorCode());
            } else {
                convertedBalance = conversionResponseDTO.getConversionResult();
            }
        } else {
            convertedBalance = transferDTO.getAmount();
        }

        //withdraw from source account and deposit to destination
        BigDecimal sourceOriginBalance = sourceAccountInfoOptional.get().getBalance();
        AccountInfo sourceAccountInfo = sourceAccountInfoOptional.get();
        BigDecimal destinationOriginBalance = destinationAccountInfoOptional.get().getBalance();
        AccountInfo destinationAccountInfo = destinationAccountInfoOptional.get();
        sourceAccountInfo.setBalance(sourceOriginBalance.subtract(transferDTO.getAmount()));
        accountInfoRepository.save(sourceAccountInfo);
        destinationAccountInfo.setBalance(destinationOriginBalance.add(convertedBalance));
        accountInfoRepository.save(destinationAccountInfo);

        // save transaction log
        TransactionLogDTO transactionLogDTO = new TransactionLogDTO(ZonedDateTime.now(ZoneId.systemDefault()), ActionType.TRANSFER,
                transferDTO.getAmount(), sourceAccountInfoOptional.get().getId(), sourceAccountInfoOptional.get().getFund().getCode(), destinationAccountInfoOptional.get().getId(), destinationAccountInfoOptional.get().getFund().getCode(), generateTrackingCode());
        TransactionLogDTO transactionLogResult = transactionLogService.save(transactionLogDTO);
        return transactionLogResult.getTrackingCode();
    }

    //todo : generating tracking code must be optimize in future
    private String generateTrackingCode() {
        UUID trackingCode = UUID.randomUUID();
        return trackingCode.toString();
    }

}

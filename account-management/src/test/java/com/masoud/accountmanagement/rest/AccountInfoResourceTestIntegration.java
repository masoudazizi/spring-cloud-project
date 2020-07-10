package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.AccountManagementApplication;
import com.masoud.accountmanagement.domain.*;
import com.masoud.accountmanagement.repository.*;
import com.masoud.accountmanagement.domain.*;
import com.masoud.accountmanagement.domain.enumeration.AccountType;
import com.masoud.accountmanagement.repository.*;
import com.masoud.accountmanagement.service.dto.TransferDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link AccountInfoResource} REST controller.
 */
@SpringBootTest(classes = {AccountManagementApplication.class})

@AutoConfigureMockMvc
@WithMockUser
public class AccountInfoResourceTestIntegration {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1000);

    private static final String DEFAULT_ACCOUNT_NUMBER = "11111";

    private static final String SOURCE_ACCOUNT_NUMBER = "123456";
    private static final String DESTINATION_ACCOUNT_NUMBER = "654321";

    private static final String DEFAULT_DESCRIPTION = "testA";

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.SAVING_ACCOUNT;

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc restAccountInfoMockMvc;

    private AccountInfo destinationAccountInfo;

    private AccountInfo sourceAccountInfo;

    /**
     * Create an entity for this test.
     */
    public AccountInfo createEntity(EntityManager entityManager) {
        BankBranch bankBranch = bankBranchRepository.saveAndFlush(BankBranchResourceTest.createUpdatedEntity(entityManager));
        Fund fund = fundRepository.saveAndFlush(FundResourceTest.createEntity(entityManager));
        Customer customer = customerRepository.saveAndFlush(CustomerResourceTest.createEntity(entityManager));
        AccountInfo accountInfo = new AccountInfo()
                .balance(DEFAULT_BALANCE)
                .accountNumber(DEFAULT_ACCOUNT_NUMBER)
                .description(DEFAULT_DESCRIPTION)
                .accountType(DEFAULT_ACCOUNT_TYPE)
                .customer(customer)
                .fund(fund)
                .bankBranch(bankBranch);
        return accountInfo;
    }

    /**
     * Create an entity for this test.
     */
    public AccountInfo createSourceEntity(EntityManager entityManager) {
        BankBranch bankBranch = bankBranchRepository.saveAndFlush(BankBranchResourceTest.createUpdatedEntity(entityManager));
        Fund fund = fundRepository.saveAndFlush(FundResourceTest.createUSDFundEntity(entityManager));
        Customer customer = customerRepository.saveAndFlush(CustomerResourceTest.createEntity(entityManager));
        AccountInfo accountInfo = new AccountInfo()
                .balance(DEFAULT_BALANCE)
                .accountNumber(SOURCE_ACCOUNT_NUMBER)
                .description(DEFAULT_DESCRIPTION)
                .accountType(DEFAULT_ACCOUNT_TYPE)
                .customer(customer)
                .fund(fund)
                .bankBranch(bankBranch);
        return accountInfo;
    }


    /**
     * Create an entity for this test.
     */
    public AccountInfo createDestinationEntity(EntityManager entityManager) {
        BankBranch bankBranch = bankBranchRepository.saveAndFlush(BankBranchResourceTest.createUpdatedEntity(entityManager));
        Fund fund = fundRepository.saveAndFlush(FundResourceTest.createINRFundEntity(entityManager));
        Customer customer = customerRepository.saveAndFlush(CustomerResourceTest.createEntity(entityManager));
        AccountInfo accountInfo = new AccountInfo()
                .balance(DEFAULT_BALANCE)
                .accountNumber(DESTINATION_ACCOUNT_NUMBER)
                .description(DEFAULT_DESCRIPTION)
                .accountType(DEFAULT_ACCOUNT_TYPE)
                .customer(customer)
                .fund(fund)
                .bankBranch(bankBranch);
        return accountInfo;
    }

    @BeforeEach
    public void initTest() {
        destinationAccountInfo = createDestinationEntity(entityManager);
        sourceAccountInfo = createSourceEntity(entityManager);
    }

    @Test
    @Transactional
    public void transfer() throws Exception {

        int sizeOfTransactionBeforeSaveAccountInfo = transactionLogRepository.findAll().size();

        // save destination and source account info
        accountInfoRepository.saveAndFlush(destinationAccountInfo);
        accountInfoRepository.saveAndFlush(sourceAccountInfo);

        // create transfer accountInfo
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setDestinationAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        transferDTO.setSourceAccountNumber(SOURCE_ACCOUNT_NUMBER);
        transferDTO.setAmount(new BigDecimal(50));

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountInfoMockMvc.perform(post("/api/transfer")
                .contentType(MediaType.APPLICATION_JSON).with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
                .andExpect(status().isCreated());

        // Validate the AccountInfo in the database
        Optional<AccountInfo> destinationAccountInfo = accountInfoRepository.findByAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        if (destinationAccountInfo.get() != null) {
            assertThat(destinationAccountInfo.get().getBalance()).isGreaterThan(DEFAULT_BALANCE);
        }
        Optional<AccountInfo> sourceAccountInfo = accountInfoRepository.findByAccountNumber(SOURCE_ACCOUNT_NUMBER);
        if (sourceAccountInfo.get() != null) {
            assertThat(sourceAccountInfo.get().getBalance()).isEqualTo(DEFAULT_BALANCE.subtract(new BigDecimal(50)));
        }

        // Validate the TransactionLong in the database
        List<TransactionLog> transactionAfterSaveAccountInfo = transactionLogRepository.findAll();
        assertThat(transactionAfterSaveAccountInfo).hasSize(sizeOfTransactionBeforeSaveAccountInfo + 1);
    }

    @Test
    @Transactional
    public void transferInsufficientBalance() throws Exception {

        // save destination and source account info
        accountInfoRepository.saveAndFlush(destinationAccountInfo);
        accountInfoRepository.saveAndFlush(sourceAccountInfo);

        // create transfer accountInfo
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setDestinationAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        transferDTO.setSourceAccountNumber(SOURCE_ACCOUNT_NUMBER);
        transferDTO.setAmount(new BigDecimal(2000));

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountInfoMockMvc.perform(post("/api/transfer")
                .contentType(MediaType.APPLICATION_JSON).with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void transferAccountNumberNotFound() throws Exception {

        // save destination and source account info
        accountInfoRepository.saveAndFlush(destinationAccountInfo);
        accountInfoRepository.saveAndFlush(sourceAccountInfo);

        // create transfer accountInfo
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setDestinationAccountNumber("1");
        transferDTO.setSourceAccountNumber(SOURCE_ACCOUNT_NUMBER);
        transferDTO.setAmount(new BigDecimal(100));

        // account number not found and this request must get bad request
        restAccountInfoMockMvc.perform(post("/api/transfer")
                .contentType(MediaType.APPLICATION_JSON).with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void transferSourceAndDestinationAreTheSame() throws Exception {

        // save destination and source account info
        accountInfoRepository.saveAndFlush(destinationAccountInfo);

        // create transfer accountInfo
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setDestinationAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        transferDTO.setSourceAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        transferDTO.setAmount(new BigDecimal(100));

        // account number not found and this request must get bad request
        restAccountInfoMockMvc.perform(post("/api/transfer")
                .contentType(MediaType.APPLICATION_JSON).with(csrf())
                .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
                .andExpect(status().isBadRequest());
    }
}

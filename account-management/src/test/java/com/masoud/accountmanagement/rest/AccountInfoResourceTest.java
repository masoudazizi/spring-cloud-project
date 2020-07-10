package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.AccountManagementApplication;
import com.masoud.accountmanagement.domain.*;
import com.masoud.accountmanagement.repository.*;
import com.masoud.accountmanagement.domain.*;
import com.masoud.accountmanagement.domain.enumeration.AccountType;
import com.masoud.accountmanagement.repository.*;
import com.masoud.accountmanagement.service.dto.AccountInfoDTO;
import com.masoud.accountmanagement.service.dto.WithdrawDepositDTO;
import com.masoud.accountmanagement.service.mapper.AccountInfoMapper;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AccountInfoResource} REST controller.
 */
@SpringBootTest(classes = {AccountManagementApplication.class})

@AutoConfigureMockMvc
@WithMockUser
public class AccountInfoResourceTest {

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1000);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2000);

    private static final String DEFAULT_ACCOUNT_NUMBER = "11111";
    private static final String UPDATED_ACCOUNT_NUMBER = "22222";

    private static final String SOURCE_ACCOUNT_NUMBER = "123456";
    private static final String DESTINATION_ACCOUNT_NUMBER = "654321";

    private static final String DEFAULT_DESCRIPTION = "testA";
    private static final String UPDATED_DESCRIPTION = "testB";

    private static final AccountType DEFAULT_ACCOUNT_TYPE = AccountType.SAVING_ACCOUNT;
    private static final AccountType UPDATED_ACCOUNT_TYPE = AccountType.CURRENT_ACCOUNT;

    @Autowired
    private AccountInfoRepository accountInfoRepository;

    @Autowired
    private AccountInfoMapper accountInfoMapper;

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

    private AccountInfo accountInfo;

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
     * Create an updated entity for this test.
     */
    public static AccountInfo createUpdatedEntity(EntityManager entityManager) {
        AccountInfo accountInfo = new AccountInfo()
                .balance(UPDATED_BALANCE)
                .accountNumber(UPDATED_ACCOUNT_NUMBER)
                .description(UPDATED_DESCRIPTION)
                .accountType(UPDATED_ACCOUNT_TYPE);
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
        accountInfo = createEntity(entityManager);
        destinationAccountInfo = createDestinationEntity(entityManager);
        sourceAccountInfo = createSourceEntity(entityManager);
    }

    @Test
    @Transactional
    public void createAccountInfo() throws Exception {
        int databaseSizeBeforeCreate = accountInfoRepository.findAll().size();

        // Create the AccountInfo
        AccountInfoDTO accountInfoDTO = accountInfoMapper.toDto(accountInfo);
        restAccountInfoMockMvc.perform(post("/api/account-infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(accountInfoDTO)))
                .andExpect(status().isCreated());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeCreate + 1);
        AccountInfo testAccountInfo = accountInfoList.get(accountInfoList.size() - 1);
        assertThat(testAccountInfo.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testAccountInfo.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testAccountInfo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAccountInfo.getAccountType()).isEqualTo(DEFAULT_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    public void createAccountInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = accountInfoRepository.findAll().size();

        // Create the AccountInfo with an existing ID
        accountInfo.setId(1L);
        AccountInfoDTO accountInfoDTO = accountInfoMapper.toDto(accountInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountInfoMockMvc.perform(post("/api/account-infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(accountInfoDTO)))
                .andExpect(status().isBadRequest());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkUniquenessOfAccountNumber() throws Exception {

        accountInfoRepository.saveAndFlush(accountInfo);
        AccountInfoDTO accountInfoDTO = accountInfoMapper.toDto(accountInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountInfoMockMvc.perform(post("/api/account-infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(accountInfoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void withdraw() throws Exception {

        int sizeOfTransactionBeforeWithdraw = transactionLogRepository.findAll().size();

        // save destination and source account info
        accountInfoRepository.saveAndFlush(destinationAccountInfo);

        // create withdraw request
        WithdrawDepositDTO withdrawDepositDTO = new WithdrawDepositDTO();
        withdrawDepositDTO.setAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        withdrawDepositDTO.setAmount(new BigDecimal(100));


        restAccountInfoMockMvc.perform(post("/api/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
                .andExpect(status().isCreated());


        // Validate the AccountInfo in the database
        Optional<AccountInfo> destinationAccountInfo = accountInfoRepository.findByAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        if (destinationAccountInfo.get() != null) {
            assertThat(destinationAccountInfo.get().getBalance()).isEqualTo(DEFAULT_BALANCE.subtract(new BigDecimal(100)));
        }

        // Validate the TransactionLong in the database
        List<TransactionLog> transactionAfterWithdraw = transactionLogRepository.findAll();
        assertThat(transactionAfterWithdraw).hasSize(sizeOfTransactionBeforeWithdraw + 1);
    }

    @Test
    @Transactional
    public void withdrawInsufficientBalance() throws Exception {

        // save destination and source account info
        accountInfoRepository.saveAndFlush(destinationAccountInfo);

        // create withdraw request
        WithdrawDepositDTO withdrawDepositDTO = new WithdrawDepositDTO();
        withdrawDepositDTO.setAccountNumber(DESTINATION_ACCOUNT_NUMBER);
        withdrawDepositDTO.setAmount(new BigDecimal(10000));


        restAccountInfoMockMvc.perform(post("/api/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void withdrawAccountNotFound() throws Exception {

        // save destination and source account info
        accountInfoRepository.saveAndFlush(destinationAccountInfo);

        // create withdraw request
        WithdrawDepositDTO withdrawDepositDTO = new WithdrawDepositDTO();
        withdrawDepositDTO.setAccountNumber("22");
        withdrawDepositDTO.setAmount(new BigDecimal(10));


        restAccountInfoMockMvc.perform(post("/api/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Transactional
    public void getAllAccountInfos() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);

        // Get all the accountInfoList
        restAccountInfoMockMvc.perform(get("/api/account-infos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(accountInfo.getId().intValue())))
                .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
                .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
                .andExpect(jsonPath("$.[*].accountType").value(hasItem(DEFAULT_ACCOUNT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void deposit() throws Exception {

        int sizeOfTransactionBeforeDeposit = transactionLogRepository.findAll().size();
        // save destination and source account info
        accountInfoRepository.saveAndFlush(sourceAccountInfo);

        // create withdraw request
        WithdrawDepositDTO withdrawDepositDTO = new WithdrawDepositDTO();
        withdrawDepositDTO.setAccountNumber(SOURCE_ACCOUNT_NUMBER);
        withdrawDepositDTO.setAmount(new BigDecimal(100));


        restAccountInfoMockMvc.perform(post("/api/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
                .andExpect(status().isCreated());


        // Validate the AccountInfo in the database
        Optional<AccountInfo> sourceAccountInfo = accountInfoRepository.findByAccountNumber(SOURCE_ACCOUNT_NUMBER);
        if (sourceAccountInfo.get() != null) {
            assertThat(sourceAccountInfo.get().getBalance()).isEqualTo(DEFAULT_BALANCE.add(new BigDecimal(100)));
        }

        // Validate the TransactionLong in the database
        List<TransactionLog> transactionAfterDeposit = transactionLogRepository.findAll();
        assertThat(transactionAfterDeposit).hasSize(sizeOfTransactionBeforeDeposit + 1);
    }

    @Test
    @Transactional
    public void depositAccountNumberNotFound() throws Exception {

        // save destination and source account info
        accountInfoRepository.saveAndFlush(sourceAccountInfo);

        // create withdraw request
        WithdrawDepositDTO withdrawDepositDTO = new WithdrawDepositDTO();
        withdrawDepositDTO.setAccountNumber("4566");
        withdrawDepositDTO.setAmount(new BigDecimal(100));


        restAccountInfoMockMvc.perform(post("/api/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(withdrawDepositDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void getAccountInfo() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);

        // Get the accountInfo
        restAccountInfoMockMvc.perform(get("/api/account-infos/{id}", accountInfo.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(accountInfo.getId().intValue()))
                .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
                .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.accountType").value(DEFAULT_ACCOUNT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAccountInfo() throws Exception {
        // Get the accountInfo
        restAccountInfoMockMvc.perform(get("/api/account-infos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAccountInfo() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);

        int databaseSizeBeforeUpdate = accountInfoRepository.findAll().size();

        // Update the accountInfo
        AccountInfo updatedAccountInfo = accountInfoRepository.findById(accountInfo.getId()).get();
        // Disconnect from session so that the updates on updatedAccountInfo are not directly saved in db
        entityManager.detach(updatedAccountInfo);
        updatedAccountInfo
                .balance(UPDATED_BALANCE)
                .accountNumber(UPDATED_ACCOUNT_NUMBER)
                .description(UPDATED_DESCRIPTION)
                .accountType(UPDATED_ACCOUNT_TYPE);
        AccountInfoDTO accountInfoDTO = accountInfoMapper.toDto(updatedAccountInfo);

        restAccountInfoMockMvc.perform(put("/api/account-infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(accountInfoDTO)))
                .andExpect(status().isOk());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeUpdate);
        AccountInfo testAccountInfo = accountInfoList.get(accountInfoList.size() - 1);
        assertThat(testAccountInfo.getBalance()).isEqualTo(UPDATED_BALANCE);
        assertThat(testAccountInfo.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testAccountInfo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAccountInfo.getAccountType()).isEqualTo(UPDATED_ACCOUNT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAccountInfo() throws Exception {
        int databaseSizeBeforeUpdate = accountInfoRepository.findAll().size();

        // Create the AccountInfo
        AccountInfoDTO accountInfoDTO = accountInfoMapper.toDto(accountInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountInfoMockMvc.perform(put("/api/account-infos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(accountInfoDTO)))
                .andExpect(status().isBadRequest());

        // Validate the AccountInfo in the database
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAccountInfo() throws Exception {
        // Initialize the database
        accountInfoRepository.saveAndFlush(accountInfo);

        int databaseSizeBeforeDelete = accountInfoRepository.findAll().size();

        // Delete the accountInfo
        restAccountInfoMockMvc.perform(delete("/api/account-infos/{id}", accountInfo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountInfo> accountInfoList = accountInfoRepository.findAll();
        assertThat(accountInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

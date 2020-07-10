package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.AccountManagementApplication;
import com.masoud.accountmanagement.domain.TransactionLog;
import com.masoud.accountmanagement.domain.enumeration.ActionType;
import com.masoud.accountmanagement.domain.enumeration.FundCode;
import com.masoud.accountmanagement.repository.TransactionLogRepository;
import com.masoud.accountmanagement.service.dto.TransactionLogDTO;
import com.masoud.accountmanagement.service.mapper.TransactionLogMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TransactionLogResource} REST controller.
 */
@SpringBootTest(classes = {AccountManagementApplication.class})

@AutoConfigureMockMvc
@WithMockUser
public class TransactionLogResourceTest {

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);

    private static final ActionType DEFAULT_ACTION_TYPE = ActionType.WITHDRAW;

    private static final Long DEFAULT_BALANCE = 1L;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";

    private static final FundCode DEFAULT_FUND_CODE = FundCode.USD;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";

    private static final String DEFAULT_TRACKING_CODE = UUID.randomUUID().toString();

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private TransactionLogMapper transactionLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionLogMockMvc;

    private TransactionLog transactionLog;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionLog createEntity(EntityManager em) {
        TransactionLog transactionLog = new TransactionLog()
                .time(DEFAULT_TIME)
                .actionType(DEFAULT_ACTION_TYPE)
                .balance(DEFAULT_BALANCE)
                .status(DEFAULT_STATUS)
                .fromFund(DEFAULT_FUND_CODE)
                .toFund(DEFAULT_FUND_CODE)
                .time(DEFAULT_TIME)
                .trackingCode(DEFAULT_TRACKING_CODE)
                .description(DEFAULT_DESCRIPTION);
        return transactionLog;
    }

    @BeforeEach
    public void initTest() {
        transactionLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransactionLog() throws Exception {
        int databaseSizeBeforeCreate = transactionLogRepository.findAll().size();

        // Create the TransactionLog
        TransactionLogDTO transactionLogDTO = transactionLogMapper.toDto(transactionLog);
        restTransactionLogMockMvc.perform(post("/api/transaction-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(transactionLogDTO)))
                .andExpect(status().isCreated());

        // Validate the TransactionLog in the database
        List<TransactionLog> transactionLogList = transactionLogRepository.findAll();
        assertThat(transactionLogList).hasSize(databaseSizeBeforeCreate + 1);
        TransactionLog testTransactionLog = transactionLogList.get(transactionLogList.size() - 1);
        assertThat(testTransactionLog.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testTransactionLog.getActionType()).isEqualTo(DEFAULT_ACTION_TYPE);
        assertThat(testTransactionLog.getBalance()).isEqualTo(DEFAULT_BALANCE);
        assertThat(testTransactionLog.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testTransactionLog.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createTransactionLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transactionLogRepository.findAll().size();

        // Create the TransactionLog with an existing ID
        transactionLog.setId(1L);
        TransactionLogDTO transactionLogDTO = transactionLogMapper.toDto(transactionLog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionLogMockMvc.perform(post("/api/transaction-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(transactionLogDTO)))
                .andExpect(status().isBadRequest());

        // Validate the TransactionLog in the database
        List<TransactionLog> transactionLogList = transactionLogRepository.findAll();
        assertThat(transactionLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTransactionLogs() throws Exception {
        // Initialize the database
        TransactionLog transactionLog = transactionLogRepository.saveAndFlush(this.transactionLog);
        String startDate = transactionLog.getTime().minusDays(1).toString();
        String endDate = transactionLog.getTime().plusDays(1).toString();


        // Get all the transactionLogList
        restTransactionLogMockMvc.perform(get("/api/transaction-logs/{startDate}/{endDate}?sort=id,desc"
                .replace("{startDate}", startDate).replace("{endDate}", endDate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(this.transactionLog.getId().intValue())))
                .andExpect(jsonPath("$.[*].actionType").value(hasItem(DEFAULT_ACTION_TYPE.toString())))
                .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void getTransactionLog() throws Exception {
        // Initialize the database
        transactionLogRepository.saveAndFlush(transactionLog);

        // Get the transactionLog
        restTransactionLogMockMvc.perform(get("/api/transaction-logs/{id}", transactionLog.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(transactionLog.getId().intValue()))
                .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
                .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
                .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionLog() throws Exception {
        // Get the transactionLog
        restTransactionLogMockMvc.perform(get("/api/transaction-logs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void getTransactionLogByTrackingCode() throws Exception {
        // Initialize the database
        transactionLogRepository.saveAndFlush(transactionLog);

        // Get the transactionLog
        restTransactionLogMockMvc.perform(get("/api/transaction-logs/tracking-code/{trackingCode}", transactionLog.getTrackingCode()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(transactionLog.getId().intValue()))
                .andExpect(jsonPath("$.actionType").value(DEFAULT_ACTION_TYPE.toString()))
                .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()))
                .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    public void getNonExistingTransactionLogByTrackingCode() throws Exception {
        // Get the transactionLog
        restTransactionLogMockMvc.perform(get("/api/transaction-logs/tracking-code/{trackingCode}", UUID.randomUUID().toString()))
                .andExpect(status().isNotFound());
    }


}

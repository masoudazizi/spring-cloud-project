package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.AccountManagementApplication;
import com.masoud.accountmanagement.domain.Fund;
import com.masoud.accountmanagement.domain.enumeration.FundCode;
import com.masoud.accountmanagement.repository.FundRepository;
import com.masoud.accountmanagement.service.dto.FundDTO;
import com.masoud.accountmanagement.service.mapper.FundMapper;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FundResource} REST controller.
 */
@SpringBootTest(classes = { AccountManagementApplication.class})

@AutoConfigureMockMvc
@WithMockUser
public class FundResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final FundCode DEFAULT_CODE = FundCode.USD;
    private static final FundCode UPDATED_CODE = FundCode.EUR;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private FundMapper fundMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFundMockMvc;

    private Fund fund;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fund createEntity(EntityManager em) {
        Fund fund = new Fund()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE);
        return fund;
    }

    /**
     * Create an entity for using in account operation.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fund createUSDFundEntity(EntityManager em) {
        Fund fund = new Fund()
            .name(DEFAULT_NAME)
            .code(FundCode.USD);
        return fund;
    }

    /**
     * Create an entity for using in account operation.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fund createINRFundEntity(EntityManager em) {
        Fund fund = new Fund()
            .name(DEFAULT_NAME)
            .code(FundCode.EUR);
        return fund;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fund createUpdatedEntity(EntityManager em) {
        Fund fund = new Fund()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);
        return fund;
    }

    @BeforeEach
    public void initTest() {
        fund = createEntity(em);
    }

    @Test
    @Transactional
    public void createFund() throws Exception {
        int databaseSizeBeforeCreate = fundRepository.findAll().size();

        // Create the Fund
        FundDTO fundDTO = fundMapper.toDto(fund);
        restFundMockMvc.perform(post("/api/funds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundDTO)))
            .andExpect(status().isCreated());

        // Validate the Fund in the database
        List<Fund> fundList = fundRepository.findAll();
        assertThat(fundList).hasSize(databaseSizeBeforeCreate + 1);
        Fund testFund = fundList.get(fundList.size() - 1);
        assertThat(testFund.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFund.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createFundWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fundRepository.findAll().size();

        // Create the Fund with an existing ID
        fund.setId(1L);
        FundDTO fundDTO = fundMapper.toDto(fund);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFundMockMvc.perform(post("/api/funds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fund in the database
        List<Fund> fundList = fundRepository.findAll();
        assertThat(fundList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFunds() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        // Get all the fundList
        restFundMockMvc.perform(get("/api/funds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fund.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        // Get the fund
        restFundMockMvc.perform(get("/api/funds/{id}", fund.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fund.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingFund() throws Exception {
        // Get the fund
        restFundMockMvc.perform(get("/api/funds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        int databaseSizeBeforeUpdate = fundRepository.findAll().size();

        // Update the fund
        Fund updatedFund = fundRepository.findById(fund.getId()).get();
        // Disconnect from session so that the updates on updatedFund are not directly saved in db
        em.detach(updatedFund);
        updatedFund
            .name(UPDATED_NAME)
            .code(UPDATED_CODE);
        FundDTO fundDTO = fundMapper.toDto(updatedFund);

        restFundMockMvc.perform(put("/api/funds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundDTO)))
            .andExpect(status().isOk());

        // Validate the Fund in the database
        List<Fund> fundList = fundRepository.findAll();
        assertThat(fundList).hasSize(databaseSizeBeforeUpdate);
        Fund testFund = fundList.get(fundList.size() - 1);
        assertThat(testFund.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFund.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingFund() throws Exception {
        int databaseSizeBeforeUpdate = fundRepository.findAll().size();

        // Create the Fund
        FundDTO fundDTO = fundMapper.toDto(fund);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFundMockMvc.perform(put("/api/funds")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(fundDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fund in the database
        List<Fund> fundList = fundRepository.findAll();
        assertThat(fundList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFund() throws Exception {
        // Initialize the database
        fundRepository.saveAndFlush(fund);

        int databaseSizeBeforeDelete = fundRepository.findAll().size();

        // Delete the fund
        restFundMockMvc.perform(delete("/api/funds/{id}", fund.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fund> fundList = fundRepository.findAll();
        assertThat(fundList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

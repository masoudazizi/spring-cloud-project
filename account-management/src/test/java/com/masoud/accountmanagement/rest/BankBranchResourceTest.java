package com.masoud.accountmanagement.rest;

import com.masoud.accountmanagement.AccountManagementApplication;
import com.masoud.accountmanagement.repository.BankBranchRepository;
import com.masoud.accountmanagement.repository.BankRepository;
import com.masoud.accountmanagement.service.dto.BankBranchDTO;
import com.masoud.accountmanagement.service.mapper.BankBranchMapper;
import com.masoud.accountmanagement.domain.Bank;
import com.masoud.accountmanagement.domain.BankBranch;

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
 * Integration tests for the {@link BankBranchResource} REST controller.
 */
@SpringBootTest(classes = {AccountManagementApplication.class})

@AutoConfigureMockMvc
@WithMockUser
public class BankBranchResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private BankBranchRepository bankBranchRepository;

    @Autowired
    private BankBranchMapper bankBranchMapper;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankBranchMockMvc;

    private BankBranch bankBranch;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public BankBranch createEntity(EntityManager em) {
        Bank bank = bankRepository.saveAndFlush(BankResourceTest.createEntity(em));
        BankBranch bankBranch = new BankBranch()
                .name(DEFAULT_NAME)
                .code(DEFAULT_CODE)
                .phone(DEFAULT_PHONE)
                .bank(bank);
        return bankBranch;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankBranch createUpdatedEntity(EntityManager em) {
        BankBranch bankBranch = new BankBranch()
                .name(UPDATED_NAME)
                .code(UPDATED_CODE)
                .phone(UPDATED_PHONE);
        return bankBranch;
    }

    @BeforeEach
    public void initTest() {
        bankBranch = createEntity(em);
    }

    @Test
    @Transactional
    public void createBankBranch() throws Exception {
        int databaseSizeBeforeCreate = bankBranchRepository.findAll().size();

        // Create the BankBranch
        BankBranchDTO bankBranchDTO = bankBranchMapper.toDto(bankBranch);
        restBankBranchMockMvc.perform(post("/api/bank-branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(bankBranchDTO)))
                .andExpect(status().isCreated());

        // Validate the BankBranch in the database
        List<BankBranch> bankBranchList = bankBranchRepository.findAll();
        assertThat(bankBranchList).hasSize(databaseSizeBeforeCreate + 1);
        BankBranch testBankBranch = bankBranchList.get(bankBranchList.size() - 1);
        assertThat(testBankBranch.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBankBranch.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testBankBranch.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createBankBranchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bankBranchRepository.findAll().size();

        // Create the BankBranch with an existing ID
        bankBranch.setId(1L);
        BankBranchDTO bankBranchDTO = bankBranchMapper.toDto(bankBranch);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankBranchMockMvc.perform(post("/api/bank-branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(bankBranchDTO)))
                .andExpect(status().isBadRequest());

        // Validate the BankBranch in the database
        List<BankBranch> bankBranchList = bankBranchRepository.findAll();
        assertThat(bankBranchList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBankBranches() throws Exception {
        // Initialize the database
        bankBranchRepository.saveAndFlush(bankBranch);

        // Get all the bankBranchList
        restBankBranchMockMvc.perform(get("/api/bank-branches?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bankBranch.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    public void getBankBranch() throws Exception {
        // Initialize the database
        bankBranchRepository.saveAndFlush(bankBranch);

        // Get the bankBranch
        restBankBranchMockMvc.perform(get("/api/bank-branches/{id}", bankBranch.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(bankBranch.getId().intValue()))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
                .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    public void getNonExistingBankBranch() throws Exception {
        // Get the bankBranch
        restBankBranchMockMvc.perform(get("/api/bank-branches/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBankBranch() throws Exception {
        // Initialize the database
        bankBranchRepository.saveAndFlush(bankBranch);

        int databaseSizeBeforeUpdate = bankBranchRepository.findAll().size();

        // Update the bankBranch
        BankBranch updatedBankBranch = bankBranchRepository.findById(bankBranch.getId()).get();
        // Disconnect from session so that the updates on updatedBankBranch are not directly saved in db
        em.detach(updatedBankBranch);
        updatedBankBranch
                .name(UPDATED_NAME)
                .code(UPDATED_CODE)
                .phone(UPDATED_PHONE);
        BankBranchDTO bankBranchDTO = bankBranchMapper.toDto(updatedBankBranch);

        restBankBranchMockMvc.perform(put("/api/bank-branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(bankBranchDTO)))
                .andExpect(status().isOk());

        // Validate the BankBranch in the database
        List<BankBranch> bankBranchList = bankBranchRepository.findAll();
        assertThat(bankBranchList).hasSize(databaseSizeBeforeUpdate);
        BankBranch testBankBranch = bankBranchList.get(bankBranchList.size() - 1);
        assertThat(testBankBranch.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBankBranch.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testBankBranch.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingBankBranch() throws Exception {
        int databaseSizeBeforeUpdate = bankBranchRepository.findAll().size();

        // Create the BankBranch
        BankBranchDTO bankBranchDTO = bankBranchMapper.toDto(bankBranch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankBranchMockMvc.perform(put("/api/bank-branches")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(bankBranchDTO)))
                .andExpect(status().isBadRequest());

        // Validate the BankBranch in the database
        List<BankBranch> bankBranchList = bankBranchRepository.findAll();
        assertThat(bankBranchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBankBranch() throws Exception {
        // Initialize the database
        bankBranchRepository.saveAndFlush(bankBranch);

        int databaseSizeBeforeDelete = bankBranchRepository.findAll().size();

        // Delete the bankBranch
        restBankBranchMockMvc.perform(delete("/api/bank-branches/{id}", bankBranch.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BankBranch> bankBranchList = bankBranchRepository.findAll();
        assertThat(bankBranchList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

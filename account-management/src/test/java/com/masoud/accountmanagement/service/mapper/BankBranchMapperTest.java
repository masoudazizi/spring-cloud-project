package com.masoud.accountmanagement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BankBranchMapperTest {

    private BankBranchMapper bankBranchMapper;

    @BeforeEach
    public void setUp() {
        bankBranchMapper = new BankBranchMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bankBranchMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bankBranchMapper.fromId(null)).isNull();
    }
}

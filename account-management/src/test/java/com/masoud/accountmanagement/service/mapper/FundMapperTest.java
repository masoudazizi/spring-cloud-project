package com.masoud.accountmanagement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FundMapperTest {

    private FundMapper fundMapper;

    @BeforeEach
    public void setUp() {
        fundMapper = new FundMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fundMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(fundMapper.fromId(null)).isNull();
    }
}

package com.masoud.accountmanagement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BankMapperTest {

    private BankMapper bankMapper;

    @BeforeEach
    public void setUp() {
        bankMapper = new BankMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bankMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bankMapper.fromId(null)).isNull();
    }
}

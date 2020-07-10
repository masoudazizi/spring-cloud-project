package com.masoud.accountmanagement.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionLogMapperTest {

    private TransactionLogMapper transactionLogMapper;

    @BeforeEach
    public void setUp() {
        transactionLogMapper = new TransactionLogMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(transactionLogMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(transactionLogMapper.fromId(null)).isNull();
    }
}

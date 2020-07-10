package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.rest.TestUtil;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionLogDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionLogDTO.class);
        TransactionLogDTO transactionLogDTO1 = new TransactionLogDTO();
        transactionLogDTO1.setId(1L);
        TransactionLogDTO transactionLogDTO2 = new TransactionLogDTO();
        assertThat(transactionLogDTO1).isNotEqualTo(transactionLogDTO2);
        transactionLogDTO2.setId(transactionLogDTO1.getId());
        assertThat(transactionLogDTO1).isEqualTo(transactionLogDTO2);
        transactionLogDTO2.setId(2L);
        assertThat(transactionLogDTO1).isNotEqualTo(transactionLogDTO2);
        transactionLogDTO1.setId(null);
        assertThat(transactionLogDTO1).isNotEqualTo(transactionLogDTO2);
    }
}

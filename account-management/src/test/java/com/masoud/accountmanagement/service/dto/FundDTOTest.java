package com.masoud.accountmanagement.service.dto;

import com.masoud.accountmanagement.rest.TestUtil;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FundDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FundDTO.class);
        FundDTO fundDTO1 = new FundDTO();
        fundDTO1.setId(1L);
        FundDTO fundDTO2 = new FundDTO();
        assertThat(fundDTO1).isNotEqualTo(fundDTO2);
        fundDTO2.setId(fundDTO1.getId());
        assertThat(fundDTO1).isEqualTo(fundDTO2);
        fundDTO2.setId(2L);
        assertThat(fundDTO1).isNotEqualTo(fundDTO2);
        fundDTO1.setId(null);
        assertThat(fundDTO1).isNotEqualTo(fundDTO2);
    }
}

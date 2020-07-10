package com.masoud.accountmanagement.domain;

import com.masoud.accountmanagement.rest.TestUtil;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AccountInfoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountInfo.class);
        AccountInfo accountInfo1 = new AccountInfo();
        accountInfo1.setId(1L);
        AccountInfo accountInfo2 = new AccountInfo();
        accountInfo2.setId(accountInfo1.getId());
        assertThat(accountInfo1).isEqualTo(accountInfo2);
        accountInfo2.setId(2L);
        assertThat(accountInfo1).isNotEqualTo(accountInfo2);
        accountInfo1.setId(null);
        assertThat(accountInfo1).isNotEqualTo(accountInfo2);
    }
}

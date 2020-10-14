package com.concesionario3.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.concesionario3.web.rest.TestUtil;

public class CocheTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coche.class);
        Coche coche1 = new Coche();
        coche1.setId(1L);
        Coche coche2 = new Coche();
        coche2.setId(coche1.getId());
        assertThat(coche1).isEqualTo(coche2);
        coche2.setId(2L);
        assertThat(coche1).isNotEqualTo(coche2);
        coche1.setId(null);
        assertThat(coche1).isNotEqualTo(coche2);
    }
}

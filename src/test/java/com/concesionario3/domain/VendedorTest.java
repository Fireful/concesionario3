package com.concesionario3.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.concesionario3.web.rest.TestUtil;

public class VendedorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vendedor.class);
        Vendedor vendedor1 = new Vendedor();
        vendedor1.setId(1L);
        Vendedor vendedor2 = new Vendedor();
        vendedor2.setId(vendedor1.getId());
        assertThat(vendedor1).isEqualTo(vendedor2);
        vendedor2.setId(2L);
        assertThat(vendedor1).isNotEqualTo(vendedor2);
        vendedor1.setId(null);
        assertThat(vendedor1).isNotEqualTo(vendedor2);
    }
}

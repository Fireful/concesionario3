package com.concesionario3.web.rest;

import com.concesionario3.Concesionario3App;
import com.concesionario3.domain.Venta;
import com.concesionario3.repository.VentaRepository;
import com.concesionario3.service.VentaService;
import com.concesionario3.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.concesionario3.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VentaResource} REST controller.
 */
@SpringBootTest(classes = Concesionario3App.class)
public class VentaResourceIT {

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Float DEFAULT_IMPORTE_TOTAL = 1F;
    private static final Float UPDATED_IMPORTE_TOTAL = 2F;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restVentaMockMvc;

    private Venta venta;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VentaResource ventaResource = new VentaResource(ventaService);
        this.restVentaMockMvc = MockMvcBuilders.standaloneSetup(ventaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createEntity(EntityManager em) {
        Venta venta = new Venta()
            .fecha(DEFAULT_FECHA)
            .importeTotal(DEFAULT_IMPORTE_TOTAL);
        return venta;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Venta createUpdatedEntity(EntityManager em) {
        Venta venta = new Venta()
            .fecha(UPDATED_FECHA)
            .importeTotal(UPDATED_IMPORTE_TOTAL);
        return venta;
    }

    @BeforeEach
    public void initTest() {
        venta = createEntity(em);
    }

    @Test
    @Transactional
    public void createVenta() throws Exception {
        int databaseSizeBeforeCreate = ventaRepository.findAll().size();

        // Create the Venta
        restVentaMockMvc.perform(post("/api/ventas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isCreated());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeCreate + 1);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testVenta.getImporteTotal()).isEqualTo(DEFAULT_IMPORTE_TOTAL);
    }

    @Test
    @Transactional
    public void createVentaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ventaRepository.findAll().size();

        // Create the Venta with an existing ID
        venta.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVentaMockMvc.perform(post("/api/ventas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVentas() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        // Get all the ventaList
        restVentaMockMvc.perform(get("/api/ventas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(venta.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())))
            .andExpect(jsonPath("$.[*].importeTotal").value(hasItem(DEFAULT_IMPORTE_TOTAL.doubleValue())));
    }

    @Test
    @Transactional
    public void getVenta() throws Exception {
        // Initialize the database
        ventaRepository.saveAndFlush(venta);

        // Get the venta
        restVentaMockMvc.perform(get("/api/ventas/{id}", venta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(venta.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()))
            .andExpect(jsonPath("$.importeTotal").value(DEFAULT_IMPORTE_TOTAL.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVenta() throws Exception {
        // Get the venta
        restVentaMockMvc.perform(get("/api/ventas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVenta() throws Exception {
        // Initialize the database
        ventaService.save(venta);

        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();

        // Update the venta
        Venta updatedVenta = ventaRepository.findById(venta.getId()).get();
        // Disconnect from session so that the updates on updatedVenta are not directly saved in db
        em.detach(updatedVenta);
        updatedVenta
            .fecha(UPDATED_FECHA)
            .importeTotal(UPDATED_IMPORTE_TOTAL);

        restVentaMockMvc.perform(put("/api/ventas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVenta)))
            .andExpect(status().isOk());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
        Venta testVenta = ventaList.get(ventaList.size() - 1);
        assertThat(testVenta.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testVenta.getImporteTotal()).isEqualTo(UPDATED_IMPORTE_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingVenta() throws Exception {
        int databaseSizeBeforeUpdate = ventaRepository.findAll().size();

        // Create the Venta

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVentaMockMvc.perform(put("/api/ventas")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(venta)))
            .andExpect(status().isBadRequest());

        // Validate the Venta in the database
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVenta() throws Exception {
        // Initialize the database
        ventaService.save(venta);

        int databaseSizeBeforeDelete = ventaRepository.findAll().size();

        // Delete the venta
        restVentaMockMvc.perform(delete("/api/ventas/{id}", venta.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Venta> ventaList = ventaRepository.findAll();
        assertThat(ventaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

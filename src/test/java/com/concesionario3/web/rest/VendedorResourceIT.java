package com.concesionario3.web.rest;

import com.concesionario3.Concesionario3App;
import com.concesionario3.domain.Vendedor;
import com.concesionario3.repository.VendedorRepository;
import com.concesionario3.service.VendedorService;
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
import java.util.List;

import static com.concesionario3.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VendedorResource} REST controller.
 */
@SpringBootTest(classes = Concesionario3App.class)
public class VendedorResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DNI = "AAAAAAAAAA";
    private static final String UPDATED_DNI = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_VENTAS = 1D;
    private static final Double UPDATED_TOTAL_VENTAS = 2D;

    @Autowired
    private VendedorRepository vendedorRepository;

    @Autowired
    private VendedorService vendedorService;

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

    private MockMvc restVendedorMockMvc;

    private Vendedor vendedor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VendedorResource vendedorResource = new VendedorResource(vendedorService);
        this.restVendedorMockMvc = MockMvcBuilders.standaloneSetup(vendedorResource)
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
    public static Vendedor createEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .nombre(DEFAULT_NOMBRE)
            .dni(DEFAULT_DNI)
            .totalVentas(DEFAULT_TOTAL_VENTAS);
        return vendedor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendedor createUpdatedEntity(EntityManager em) {
        Vendedor vendedor = new Vendedor()
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .totalVentas(UPDATED_TOTAL_VENTAS);
        return vendedor;
    }

    @BeforeEach
    public void initTest() {
        vendedor = createEntity(em);
    }

    @Test
    @Transactional
    public void createVendedor() throws Exception {
        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();

        // Create the Vendedor
        restVendedorMockMvc.perform(post("/api/vendedors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isCreated());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate + 1);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testVendedor.getDni()).isEqualTo(DEFAULT_DNI);
        assertThat(testVendedor.getTotalVentas()).isEqualTo(DEFAULT_TOTAL_VENTAS);
    }

    @Test
    @Transactional
    public void createVendedorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vendedorRepository.findAll().size();

        // Create the Vendedor with an existing ID
        vendedor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendedorMockMvc.perform(post("/api/vendedors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVendedors() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get all the vendedorList
        restVendedorMockMvc.perform(get("/api/vendedors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendedor.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].dni").value(hasItem(DEFAULT_DNI)))
            .andExpect(jsonPath("$.[*].totalVentas").value(hasItem(DEFAULT_TOTAL_VENTAS.doubleValue())));
    }

    @Test
    @Transactional
    public void getVendedor() throws Exception {
        // Initialize the database
        vendedorRepository.saveAndFlush(vendedor);

        // Get the vendedor
        restVendedorMockMvc.perform(get("/api/vendedors/{id}", vendedor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendedor.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.dni").value(DEFAULT_DNI))
            .andExpect(jsonPath("$.totalVentas").value(DEFAULT_TOTAL_VENTAS.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVendedor() throws Exception {
        // Get the vendedor
        restVendedorMockMvc.perform(get("/api/vendedors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVendedor() throws Exception {
        // Initialize the database
        vendedorService.save(vendedor);

        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Update the vendedor
        Vendedor updatedVendedor = vendedorRepository.findById(vendedor.getId()).get();
        // Disconnect from session so that the updates on updatedVendedor are not directly saved in db
        em.detach(updatedVendedor);
        updatedVendedor
            .nombre(UPDATED_NOMBRE)
            .dni(UPDATED_DNI)
            .totalVentas(UPDATED_TOTAL_VENTAS);

        restVendedorMockMvc.perform(put("/api/vendedors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVendedor)))
            .andExpect(status().isOk());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
        Vendedor testVendedor = vendedorList.get(vendedorList.size() - 1);
        assertThat(testVendedor.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testVendedor.getDni()).isEqualTo(UPDATED_DNI);
        assertThat(testVendedor.getTotalVentas()).isEqualTo(UPDATED_TOTAL_VENTAS);
    }

    @Test
    @Transactional
    public void updateNonExistingVendedor() throws Exception {
        int databaseSizeBeforeUpdate = vendedorRepository.findAll().size();

        // Create the Vendedor

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendedorMockMvc.perform(put("/api/vendedors")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(vendedor)))
            .andExpect(status().isBadRequest());

        // Validate the Vendedor in the database
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVendedor() throws Exception {
        // Initialize the database
        vendedorService.save(vendedor);

        int databaseSizeBeforeDelete = vendedorRepository.findAll().size();

        // Delete the vendedor
        restVendedorMockMvc.perform(delete("/api/vendedors/{id}", vendedor.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendedor> vendedorList = vendedorRepository.findAll();
        assertThat(vendedorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

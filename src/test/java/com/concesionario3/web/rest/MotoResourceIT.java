package com.concesionario3.web.rest;

import com.concesionario3.Concesionario3App;
import com.concesionario3.domain.Moto;
import com.concesionario3.repository.MotoRepository;
import com.concesionario3.service.MotoService;
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
 * Integration tests for the {@link MotoResource} REST controller.
 */
@SpringBootTest(classes = Concesionario3App.class)
public class MotoResourceIT {

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "CCCCCCCCCC";
    private static final String UPDATED_MODELO = "DDDDDDDDDD";


    private static final Integer DEFAULT_ANIO = 1;
    private static final Integer UPDATED_ANIO = 2;

    private static final Boolean DEFAULT_ELECTRICO = false;
    private static final Boolean UPDATED_ELECTRICO = true;

    private static final Float DEFAULT_PRECIO = 1F;
    private static final Float UPDATED_PRECIO = 2F;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private MotoService motoService;

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

    private MockMvc restMotoMockMvc;

    private Moto moto;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MotoResource motoResource = new MotoResource(motoService);
        this.restMotoMockMvc = MockMvcBuilders.standaloneSetup(motoResource)
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
    public static Moto createEntity(EntityManager em) {
        Moto moto = new Moto()
            .marca(DEFAULT_MARCA)
            .modelo(DEFAULT_MODELO)
            .anio(DEFAULT_ANIO)
            .precio(DEFAULT_PRECIO);
        return moto;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moto createUpdatedEntity(EntityManager em) {
        Moto moto = new Moto()
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .anio(UPDATED_ANIO)
            .precio(UPDATED_PRECIO);
        return moto;
    }

    @BeforeEach
    public void initTest() {
        moto = createEntity(em);
    }

    @Test
    @Transactional
    public void createMoto() throws Exception {
        int databaseSizeBeforeCreate = motoRepository.findAll().size();

        // Create the Moto
        restMotoMockMvc.perform(post("/api/motos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(moto)))
            .andExpect(status().isCreated());

        // Validate the Moto in the database
        List<Moto> motoList = motoRepository.findAll();
        assertThat(motoList).hasSize(databaseSizeBeforeCreate + 1);
        Moto testMoto = motoList.get(motoList.size() - 1);
        assertThat(testMoto.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testMoto.getAnio()).isEqualTo(DEFAULT_ANIO);
        assertThat(testMoto.getPrecio()).isEqualTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    public void createMotoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motoRepository.findAll().size();

        // Create the Moto with an existing ID
        moto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotoMockMvc.perform(post("/api/motos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(moto)))
            .andExpect(status().isBadRequest());

        // Validate the Moto in the database
        List<Moto> motoList = motoRepository.findAll();
        assertThat(motoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMotos() throws Exception {
        // Initialize the database
        motoRepository.saveAndFlush(moto);

        // Get all the motoList
        restMotoMockMvc.perform(get("/api/motos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moto.getId().intValue())))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.doubleValue())));
    }

    @Test
    @Transactional
    public void getMoto() throws Exception {
        // Initialize the database
        motoRepository.saveAndFlush(moto);

        // Get the moto
        restMotoMockMvc.perform(get("/api/motos/{id}", moto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moto.getId().intValue()))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA))
            .andExpect(jsonPath("$.anio").value(DEFAULT_ANIO))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMoto() throws Exception {
        // Get the moto
        restMotoMockMvc.perform(get("/api/motos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMoto() throws Exception {
        // Initialize the database
        motoService.save(moto);

        int databaseSizeBeforeUpdate = motoRepository.findAll().size();

        // Update the moto
        Moto updatedMoto = motoRepository.findById(moto.getId()).get();
        // Disconnect from session so that the updates on updatedMoto are not directly saved in db
        em.detach(updatedMoto);
        updatedMoto
            .marca(UPDATED_MARCA)
            .anio(UPDATED_ANIO)
            .precio(UPDATED_PRECIO);

        restMotoMockMvc.perform(put("/api/motos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMoto)))
            .andExpect(status().isOk());

        // Validate the Moto in the database
        List<Moto> motoList = motoRepository.findAll();
        assertThat(motoList).hasSize(databaseSizeBeforeUpdate);
        Moto testMoto = motoList.get(motoList.size() - 1);
        assertThat(testMoto.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testMoto.getAnio()).isEqualTo(UPDATED_ANIO);
        assertThat(testMoto.getPrecio()).isEqualTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    public void updateNonExistingMoto() throws Exception {
        int databaseSizeBeforeUpdate = motoRepository.findAll().size();

        // Create the Moto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMotoMockMvc.perform(put("/api/motos")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(moto)))
            .andExpect(status().isBadRequest());

        // Validate the Moto in the database
        List<Moto> motoList = motoRepository.findAll();
        assertThat(motoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMoto() throws Exception {
        // Initialize the database
        motoService.save(moto);

        int databaseSizeBeforeDelete = motoRepository.findAll().size();

        // Delete the moto
        restMotoMockMvc.perform(delete("/api/motos/{id}", moto.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Moto> motoList = motoRepository.findAll();
        assertThat(motoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

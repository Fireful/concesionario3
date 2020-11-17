package com.concesionario3.web.rest;

import com.concesionario3.domain.Moto;
import com.concesionario3.service.MotoService;
import com.concesionario3.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.concesionario3.domain.Moto}.
 */
@RestController
@RequestMapping("/api")
public class MotoResource {

    private final Logger log = LoggerFactory.getLogger(MotoResource.class);

    private static final String ENTITY_NAME = "moto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MotoService motoService;

    public MotoResource(MotoService motoService) {
        this.motoService = motoService;
    }

    /**
     * {@code POST  /motos} : Create a new moto.
     *
     * @param moto the moto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moto, or with status {@code 400 (Bad Request)} if the moto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/motos")
    public ResponseEntity<Moto> createMoto(@RequestBody Moto moto) throws URISyntaxException {
        log.debug("REST request to save Moto : {}", moto);
        if (moto.getId() != null) {
            throw new BadRequestAlertException("A new moto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Moto result = motoService.save(moto);
        return ResponseEntity.created(new URI("/api/motos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /motos} : Updates an existing moto.
     *
     * @param moto the moto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moto,
     * or with status {@code 400 (Bad Request)} if the moto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/motos")
    public ResponseEntity<Moto> updateMoto(@RequestBody Moto moto) throws URISyntaxException {
        log.debug("REST request to update Moto : {}", moto);
        if (moto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Moto result = motoService.save(moto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moto.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /motos} : get all the motos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of motos in body.
     */
    @GetMapping("/motos")
    public ResponseEntity<List<Moto>> getAllMotos(Pageable pageable) {
        log.debug("REST request to get a page of Motos");
        Page<Moto> page = motoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /motos/:id} : get the "id" moto.
     *
     * @param id the id of the moto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/motos/{id}")
    public ResponseEntity<Moto> getMoto(@PathVariable Long id) {
        log.debug("REST request to get Moto : {}", id);
        Optional<Moto> moto = motoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moto);
    }

    /**
     * {@code DELETE  /motos/:id} : delete the "id" moto.
     *
     * @param id the id of the moto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/motos/{id}")
    public ResponseEntity<Void> deleteMoto(@PathVariable Long id) {
        log.debug("REST request to delete Moto : {}", id);
        motoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/motos/{venta}/vendidos")
    public ResponseEntity<List<Moto>> getVendidos(Pageable page, @PathVariable Boolean venta){
        log.debug("REST request to get Vendidos: {}", venta);
        Page<Moto> moto=motoService.findVenta(page, venta);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), moto);
        return ResponseEntity.ok().headers(headers).body(moto.getContent());
    }

    @GetMapping("/motos/color/{color}")
    public ResponseEntity<List<Moto>> getColores(Pageable page, @PathVariable String color){
        log.debug("REST request to get colores: {}", color);
        Page<Moto> moto=motoService.findColor(page, color);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), moto);
        return ResponseEntity.ok().headers(headers).body(moto.getContent());
    }

    @GetMapping("/motos/{venta}/disponibles")
    public ResponseEntity<List<Moto>> getDisponibles(Pageable page, @PathVariable Boolean venta) {
        log.debug("REST request to get disponibles: {}", venta);
        Page<Moto> moto = motoService.findDisponibles(page, venta);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), moto);
        return ResponseEntity.ok().headers(headers).body(moto.getContent());
    }



}

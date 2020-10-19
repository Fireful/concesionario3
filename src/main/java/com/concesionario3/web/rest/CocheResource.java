package com.concesionario3.web.rest;

import com.concesionario3.domain.Coche;
import com.concesionario3.service.CocheService;
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
 * REST controller for managing {@link com.concesionario3.domain.Coche}.
 */
@RestController
@RequestMapping("/api")
public class CocheResource {

    private final Logger log = LoggerFactory.getLogger(CocheResource.class);

    private static final String ENTITY_NAME = "coche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CocheService cocheService;

    public CocheResource(CocheService cocheService) {
        this.cocheService = cocheService;
    }

    /**
     * {@code POST  /coches} : Create a new coche.
     *
     * @param coche the coche to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coche, or with status {@code 400 (Bad Request)} if the coche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coches")
    public ResponseEntity<Coche> createCoche(@RequestBody Coche coche) throws URISyntaxException {
        log.debug("REST request to save Coche : {}", coche);
        if (coche.getId() != null) {
            throw new BadRequestAlertException("A new coche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Coche result = cocheService.save(coche);
        return ResponseEntity.created(new URI("/api/coches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coches} : Updates an existing coche.
     *
     * @param coche the coche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coche,
     * or with status {@code 400 (Bad Request)} if the coche is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coches")
    public ResponseEntity<Coche> updateCoche(@RequestBody Coche coche) throws URISyntaxException {
        log.debug("REST request to update Coche : {}", coche);
        if (coche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Coche result = cocheService.save(coche);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coche.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /coches} : get all the coches.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coches in body.
     */
    @GetMapping("/coches")
    public ResponseEntity<List<Coche>> getAllCoches(Pageable pageable) {
        log.debug("REST request to get a page of Coches");
        Page<Coche> page = cocheService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coches/:id} : get the "id" coche.
     *
     * @param id the id of the coche to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coche, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coches/{id}")
    public ResponseEntity<Coche> getCoche(@PathVariable Long id) {
        log.debug("REST request to get Coche : {}", id);
        Optional<Coche> coche = cocheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coche);
    }

    /**
     * {@code DELETE  /coches/:id} : delete the "id" coche.
     *
     * @param id the id of the coche to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coches/{id}")
    public ResponseEntity<Void> deleteCoche(@PathVariable Long id) {
        log.debug("REST request to delete Coche : {}", id);
        cocheService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/coches/{venta}/vendidos")
    public ResponseEntity<List<Coche>> getVendidos(Pageable page, @PathVariable Boolean venta){
        log.debug("REST request to get venta: {}", venta);
        Page<Coche> coche=cocheService.findVenta(page, venta);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), coche);
        return ResponseEntity.ok().headers(headers).body(coche.getContent());
    }

    @GetMapping("/coches/{tipo}/electricos")
    public ResponseEntity<List<Coche>> getElectricos(Pageable page, @PathVariable String tipo){
        log.debug("REST request to get venta: {}", tipo);
        Page<Coche> coche=cocheService.findTipo(page, tipo);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), coche);
        return ResponseEntity.ok().headers(headers).body(coche.getContent());
    }

    @GetMapping("/coches/color/{color}")
    public ResponseEntity<List<Coche>> getColores(Pageable page, @PathVariable String color){
        log.debug("REST request to get venta: {}", color);
        Page<Coche> coche=cocheService.findColor(page, color);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), coche);
        return ResponseEntity.ok().headers(headers).body(coche.getContent());
    }
    


}

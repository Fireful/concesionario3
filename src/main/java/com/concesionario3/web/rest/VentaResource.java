package com.concesionario3.web.rest;

import com.concesionario3.domain.Venta;
import com.concesionario3.domain.enums.EnumEstadoVenta;
import com.concesionario3.service.ImpresionService;
import com.concesionario3.service.VentaService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.concesionario3.domain.Venta}.
 */
@RestController
@RequestMapping("/api")
public class VentaResource {

    private final Logger log = LoggerFactory.getLogger(VentaResource.class);

    private static final String ENTITY_NAME = "venta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VentaService ventaService;
    private final ImpresionService impresionService;

    public VentaResource(VentaService ventaService, ImpresionService impresionService) {
        this.ventaService = ventaService;
        this.impresionService=impresionService;
    }

    /**
     * {@code POST  /ventas} : Create a new venta.
     *
     * @param venta the venta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new venta, or with status {@code 400 (Bad Request)} if the
     *         venta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ventas")
    public ResponseEntity<Venta> createVenta(@RequestBody Venta venta) throws URISyntaxException {
        log.debug("REST request to save Venta : {}", venta);
        if (venta.getId() != null) {
            throw new BadRequestAlertException("A new venta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Venta result = ventaService.save(venta);
        return ResponseEntity
                .created(new URI("/api/ventas/" + result.getId())).headers(HeaderUtil
                        .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /ventas} : Updates an existing venta.
     *
     * @param venta the venta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated venta, or with status {@code 400 (Bad Request)} if the
     *         venta is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the venta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ventas")
    public ResponseEntity<Venta> updateVenta(@RequestBody Venta venta) throws URISyntaxException {
        log.debug("REST request to update Venta : {}", venta);
        if (venta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Venta result = ventaService.save(venta);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, venta.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /ventas} : get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of ventas in body.
     */
    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> getAllVentas(Pageable pageable) {
        log.debug("REST request to get a page of Ventas");
        Page<Venta> page = ventaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ventas/:id} : get the "id" venta.
     *
     * @param id the id of the venta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the venta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ventas/{id}")
    public ResponseEntity<Venta> getVenta(@PathVariable Long id) {
        log.debug("REST request to get Venta : {}", id);
        Optional<Venta> venta = ventaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(venta);
    }

    /**
     * {@code DELETE  /ventas/:id} : delete the "id" venta.
     *
     * @param id the id of the venta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ventas/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        log.debug("REST request to delete Venta : {}", id);
        ventaService.delete(id);
        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                .build();
    }

    @GetMapping(value = "/ventas/get-num")
    @Transactional
    public ResponseEntity<String> getNumVenta(String vehiculoSeleccionado) {
        log.debug("REST request to get num venta");
        String numVenta = ventaService.getNewNumeroVenta(vehiculoSeleccionado);
        return new ResponseEntity<>(numVenta, HttpStatus.OK);
    }

    @GetMapping(value = "/ventas/terminada/{id}")
    @Transactional
    public ResponseEntity<Void> finishVenta(@PathVariable Long id) {
        log.debug("REST request to finish Venta : {}", id);

        Venta venta = ventaService.findOne(id).get();
        if (venta.getEstadoVenta() == null) {
            venta.setEstadoVenta(EnumEstadoVenta.EN_PROCESO);
        }

        ventaService.finishVenta(venta);

        return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/ventas/terminadas")
    public ResponseEntity<List<Venta>> getTerminadas(Pageable page) {
        log.debug("REST mostrar listado de ventas terminadas: {}");
        Page<Venta> venta = ventaService.findTerminadas(page);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), venta);
        return ResponseEntity.ok().headers(headers).body(venta.getContent());
    }

    @GetMapping("/ventas/download/pdf")
    public ResponseEntity<byte[]> generaInforme() {
        log.debug("REST Generar informe de ventas terminadas: {}");
        List<Venta> venta = ventaService.findTerminadasList();
        ResponseEntity<byte[]> ventasTerminadas=impresionService.printVenta(venta);
        return ventasTerminadas;


    }

    @GetMapping("/ventas/downloadFactura/{id}")
    public ResponseEntity<byte[]> generaFactura(@PathVariable Long id) {
        log.debug("REST request to get ventas terminadas: {}");
        Venta venta = ventaService.findOne(id).get();
        ResponseEntity<byte[]> ventasTerminadas = impresionService.printFactura(venta);
        return ventasTerminadas;

    }

    @GetMapping("/ventas/{id}/vendedor")
    public ResponseEntity<byte[]> generaInformeVendedor(@PathVariable Long id) {
        log.debug("REST request to get ventas terminadas: {}");
        List<Venta> venta = ventaService.findTerminadasVendedorList(id);
        ResponseEntity<byte[]> ventasTerminadasVendedor = impresionService.printVentaVendedor(venta);
        return ventasTerminadasVendedor;

    }

}

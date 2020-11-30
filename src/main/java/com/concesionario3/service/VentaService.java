package com.concesionario3.service;

import com.concesionario3.domain.Venta;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Service Interface for managing {@link Venta}.
 */
public interface VentaService {

    public final Logger log = LoggerFactory.getLogger(VentaService.class);

    final SpringTemplateEngine templateEngine=null;

    /**
     * Save a venta.
     *
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    Venta save(Venta venta);


    Venta finishVenta(Venta venta);

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Venta> findAll(Pageable pageable);

    /**
     * Get the "id" venta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Venta> findOne(Long id);

    /**
     * Delete the "id" venta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    String getNewNumeroVenta(String vehiculoSeleccionado);

    Page<Venta> findTerminadas(Pageable page);

    List<Venta> findTerminadasList();

    List<Venta> findTerminadasVendedorList(Long id);

    public default void sacarInforme(Page<Venta> venta) {
        log.debug("Mostrando el informe");
        mostrarInforme("informes/ventasTerminadas", venta);
    }

    static void mostrarInforme(String templateName, Page<Venta> venta) {
        Context context = new Context();
        String content = templateEngine.process(templateName, context);

    }

}

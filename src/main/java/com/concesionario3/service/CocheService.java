package com.concesionario3.service;

import com.concesionario3.domain.Coche;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Coche}.
 */
public interface CocheService {

    /**
     * Save a coche.
     *
     * @param coche the entity to save.
     * @return the persisted entity.
     */
    Coche save(Coche coche);

    /**
     * Get all the coches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Coche> findAll(Pageable pageable);

    /**
     * Get the "id" coche.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Coche> findOne(Long id);

    /**
     * Delete the "id" coche.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<Coche> findVenta(Pageable page, Boolean venta);

    // Page<Coche> findElectrico(Pageable electrico);

    Page<Coche> findTipo(Pageable page, String tipo);
}

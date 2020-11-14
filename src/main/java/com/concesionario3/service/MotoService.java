package com.concesionario3.service;

import com.concesionario3.domain.Moto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Moto}.
 */
public interface MotoService {

    /**
     * Save a moto.
     *
     * @param moto the entity to save.
     * @return the persisted entity.
     */
    Moto save(Moto moto);

    /**
     * Get all the motos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Moto> findAll(Pageable pageable);

    /**
     * Get the "id" moto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Moto> findOne(Long id);

    /**
     * Delete the "id" moto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<Moto> findVenta(Pageable page, Boolean venta);

    Page<Moto> findTipo(Pageable page, String tipo);

    Page<Moto> findColor(Pageable page, String color);

    Page<Moto> findDisponibles(Pageable page, Boolean venta);


}

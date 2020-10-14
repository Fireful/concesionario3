package com.concesionario3.service.impl;

import com.concesionario3.service.VentaService;
import com.concesionario3.domain.Coche;
import com.concesionario3.domain.Venta;
import com.concesionario3.repository.VentaRepository;
import com.concesionario3.repository.CocheRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final CocheRepository cocheRepository;

    public VentaServiceImpl(VentaRepository ventaRepository, CocheRepository cocheRepository) {
        this.ventaRepository = ventaRepository;
        this.cocheRepository=cocheRepository;
    }

    /**
     * Save a venta.
     *9
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Venta save(Venta venta) {
        log.debug("Request to save Venta : {}", venta);
        Venta result = ventaRepository.save(venta);
        Optional<Coche> coche = cocheRepository.findById(venta.getCoche().getId());
        /* TODO Mirar por qué peta al modificar una ventaa poniendo el coche vacío */
        /* if(coche.isPresent())
        {
            log.debug("Entramos");
            coche.get().setVenta(venta);
            cocheRepository.save(coche.get());
        } */
        return result;
    }

    /**
     * Get all the ventas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Venta> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        return ventaRepository.findAll(pageable);
    }

    /**
     * Get one venta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findById(id);
    }

    /**
     * Delete the venta by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }
}

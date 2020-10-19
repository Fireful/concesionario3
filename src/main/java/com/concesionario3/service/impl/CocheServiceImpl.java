package com.concesionario3.service.impl;

import com.concesionario3.service.CocheService;
import com.concesionario3.domain.Coche;
import com.concesionario3.repository.CocheRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javafx.scene.control.Alert;
import net.bytebuddy.matcher.StringMatcher;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Coche}.
 */
@Service
@Transactional
public class CocheServiceImpl implements CocheService {

    private final Logger log = LoggerFactory.getLogger(CocheServiceImpl.class);

    private final CocheRepository cocheRepository;

    public CocheServiceImpl(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    /**
     * Save a coche.
     *
     * @param coche the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Coche save(Coche coche) {
        log.debug("Request to save Coche : {}", coche);
        return cocheRepository.save(coche);
    }

    /**
     * Get all the coches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Coche> findAll(Pageable pageable) {
        log.debug("Request to get all Coches");
        return cocheRepository.findAll(pageable);
    }

    /**
     * Get one coche by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Coche> findOne(Long id) {
        log.debug("Request to get Coche : {}", id);
        return cocheRepository.findById(id);
    }

    /**
     * Delete the coche by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coche : {}", id);
        cocheRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Coche> findVenta(Pageable page, Boolean venta) {
        
        if(venta){

            return cocheRepository.findAllByVendidos(page);
        } else{
            return cocheRepository.findAllByDisponibles(page);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Coche> findTipo(Pageable page, String tipo){
        if(tipo.equals("termicos" )){
            log.debug("Entramos en tipo termicos");
            return cocheRepository.findAllTermicos(page);
        } else if(tipo.equals("electricos")){
            log.debug("Entramos en tipo electricos");
            return cocheRepository.findAllElectricos(page);
        } else {
            log.debug("Entramos en tipo todos");
            return cocheRepository.findAll(page);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Coche> findColor(Pageable page, String color){
    
            return cocheRepository.findByColor(page, color);

    }
}

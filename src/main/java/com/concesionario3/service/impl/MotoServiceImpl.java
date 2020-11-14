package com.concesionario3.service.impl;

import com.concesionario3.service.MotoService;
import com.concesionario3.domain.Moto;
import com.concesionario3.repository.MotoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Moto}.
 */
@Service
@Transactional
public class MotoServiceImpl implements MotoService {

    private final Logger log = LoggerFactory.getLogger(MotoServiceImpl.class);

    private final MotoRepository motoRepository;

    public MotoServiceImpl(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    /**
     * Save a moto.
     *
     * @param moto the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Moto save(Moto moto) {
        log.debug("Request to save Moto : {}", moto);
        return motoRepository.save(moto);
    }

    /**
     * Get all the motos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Moto> findAll(Pageable pageable) {
        log.debug("Request to get all Motos");
        return motoRepository.findAll(pageable);
    }

    /**
     * Get one moto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Moto> findOne(Long id) {
        log.debug("Request to get Moto : {}", id);
        return motoRepository.findById(id);
    }

    /**
     * Delete the moto by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Moto : {}", id);
        motoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Moto> findVenta(Pageable page, Boolean venta) {

        if(venta){

            return motoRepository.findAllByVendidos(page);
        } else{
            return motoRepository.findAllByDisponibles(page);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Moto> findDisponibles(Pageable page, Boolean venta) {

        if (venta) {

            return motoRepository.findAllByVendidos(page);
        } else {
            return motoRepository.findByDisponibles(page);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Moto> findTipo(Pageable page, String tipo){
        if(tipo.equals("termicos" )){
            log.debug("Entramos en tipo termicos");
            return motoRepository.findAllTermicos(page);
        } else if(tipo.equals("electricos")){
            log.debug("Entramos en tipo electricos");
            return motoRepository.findAllElectricos(page);
        } else {
            log.debug("Entramos en tipo todos");
            return motoRepository.findAll(page);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Moto> findColor(Pageable page, String color){

            return motoRepository.findByColor(page, color);

    }
}

package com.concesionario3.service.impl;

import com.concesionario3.service.VentaService;
import com.concesionario3.domain.Venta;
import com.concesionario3.domain.enums.EnumEstadoVenta;
import com.concesionario3.domain.enums.EnumTipo;
import com.concesionario3.repository.VentaRepository;
import com.concesionario3.repository.VendedorRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final VendedorRepository vendedorRepository;

    public VentaServiceImpl(VentaRepository ventaRepository, VendedorRepository vendedorRepository) {
        this.ventaRepository = ventaRepository;
        this.vendedorRepository = vendedorRepository;
    }

    /**
     * Save a venta. 9
     *
     * @param venta the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Venta save(Venta venta) {
        log.debug("Vehículo tipo: "+venta.getTipo());
        if (venta.getTipo().toString().equals("COCHE")) {
            log.debug("Vehículo seleccionado tipo: " + venta.getTipo());
            if (venta.getNumeroVenta() == null) {
                venta.setNumeroVenta(getNewNumeroVenta("coche"));
                log.debug("Vehículo numero de venta: " + venta.getNumeroVenta());

            }
        } else {
            log.debug("Vehículo seleccionado tipo Moto: "+ venta.getTipo());
            if(venta.getNumeroVenta()==null){
                venta.setNumeroVenta(getNewNumeroVenta("moto"));
                log.debug("Vehículo numero de venta: "+venta.getNumeroVenta());
            }
        }

        if (!venta.getVendedor().getNombre().equals("")) {
            Double actualizaVenta = venta.getVendedor().getTotalVentas();
            actualizaVenta = (venta.getImporteTotal() + actualizaVenta);
            venta.getVendedor().setTotalVentas(actualizaVenta);

        }
        if (venta.getVendedor().getNumVentas() == null) {
            venta.getVendedor().setNumVentas(1);
        } else {
            Integer actualizaNumVentas = venta.getVendedor().getNumVentas();
            actualizaNumVentas = (actualizaNumVentas + 1);
            venta.getVendedor().setNumVentas(actualizaNumVentas);
        }
        if (venta.getEstadoVenta() == null) {
            venta.setEstadoVenta(EnumEstadoVenta.EN_PROCESO);
        } else {
            venta.setEstadoVenta(EnumEstadoVenta.EN_PROCESO);
        }

        vendedorRepository.save(venta.getVendedor());

        log.debug("Request to save Venta : {}", venta);
        return ventaRepository.save(venta);

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
        Optional<Venta> venta = ventaRepository.findById(id);
        if (venta.isPresent()) {
            Integer numeroVentas = venta.get().getVendedor().getNumVentas();
            Double totalVentas = venta.get().getVendedor().getTotalVentas();
            numeroVentas -= 1;
            totalVentas -= venta.get().getImporteTotal();
            venta.get().getVendedor().setNumVentas(numeroVentas);
            venta.get().getVendedor().setTotalVentas(totalVentas);
        }
        log.debug("Request to delete Venta : {}", id);
        ventaRepository.deleteById(id);
    }

    @Override
    public String getNewNumeroVenta(String vehiculoSeleccionado) {
        String numero = "";

        log.debug("vehículo Sel: "+vehiculoSeleccionado);

            log.debug("vehículo seleccionado inicial: " + vehiculoSeleccionado);
            if (vehiculoSeleccionado == null) {
                numero += "";
                /* vehiculoSeleccionado="coche"; */
            }
            log.debug("vehículo: " + vehiculoSeleccionado);
            log.debug("vehículo seleccionado inicial: " + vehiculoSeleccionado);
            if (vehiculoSeleccionado.equals("moto")) {
                numero += "M";
            } else if (vehiculoSeleccionado.equals("coche")) {
                numero += "C";
            }
            log.debug("vehículo seleccionado numero: " + numero);
            numero += "00";

            Page<Venta> numeros = ventaRepository.findAll(PageRequest.of(0, 1, Direction.DESC, "id"));
            numero += ((numeros.getContent().get(0).getId()) + 1);

            Calendar rightNow = Calendar.getInstance();
            numero += String.valueOf(rightNow.get(Calendar.YEAR));

        return numero;

    }

    @Override
    public Venta finishVenta(Venta venta) {
        if (venta.getEstadoVenta() == null) {

        } else {
            venta.setEstadoVenta(EnumEstadoVenta.TERMINADA);
        }

        return ventaRepository.save(venta);

    }
}

package com.concesionario3.repository;

import com.concesionario3.domain.Venta;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.vendedor=?1")
    Long countByName(String nombre);
}

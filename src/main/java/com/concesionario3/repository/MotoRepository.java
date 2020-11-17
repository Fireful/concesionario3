package com.concesionario3.repository;

import com.concesionario3.domain.Moto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Moto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
    @Query("SELECT c FROM Moto c INNER JOIN Venta v ON v.moto=c.id")
    Page<Moto> findAllByVendidos(Pageable page);

    @Query("SELECT c FROM Moto c WHERE c.id NOT IN (SELECT cs FROM Moto cs INNER JOIN Venta v ON v.moto = cs.id)")
    Page<Moto> findAllByDisponibles(Pageable disp);

    @Query("SELECT c FROM Moto c WHERE c.id NOT IN (SELECT cs FROM Moto cs INNER JOIN Venta v ON v.moto = cs.id)")
    Page<Moto> findByDisponibles(Pageable disp);

    /* @Query("SELECT c FROM Moto c INNER JOIN Venta v ON v.moto=c.id")
    Page<Moto> findAllByDisponibles(Pageable disp);
     */


     Page<Moto> findByColor(Pageable page, String color);


}

package com.concesionario3.repository;

import com.concesionario3.domain.Coche;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Coche entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CocheRepository extends JpaRepository<Coche, Long> {
    @Query("SELECT c FROM Coche c INNER JOIN Venta v ON v.coche=c.id")
    Page<Coche> findAllByVendidos(Pageable page);
    
    @Query("SELECT c FROM Coche c WHERE c.id NOT IN (SELECT cs FROM Coche cs INNER JOIN Venta v ON v.coche = cs.id)")
    Page<Coche> findAllByDisponibles(Pageable disp);

    /* @Query("SELECT c FROM Coche c INNER JOIN Venta v ON v.coche=c.id")
    Page<Coche> findAllByDisponibles(Pageable disp);
     */

     @Query("SELECT c FROM Coche c WHERE c.electrico=1")
     Page<Coche> findAllElectricos(Pageable page);

     @Query("SELECT c FROM Coche c WHERE c.electrico=0")
     Page<Coche> findAllTermicos(Pageable page);
     
     
     Page<Coche> findByColor(Pageable page, String color);
     

}

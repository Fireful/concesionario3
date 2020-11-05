package com.concesionario3.domain;

import com.concesionario3.domain.enums.EnumMetodoPago;
import com.concesionario3.domain.enums.EnumEstadoVenta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;


/**
 * A Venta.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private Instant fecha;

    @Column(name = "importe_total")
    private Float importeTotal;

    @OneToOne
    @JoinColumn(name = "coche_id")
    @JsonIgnoreProperties(value = "venta")
    private Coche coche;

    @ManyToOne
    @JsonIgnoreProperties("ventas")
    private Cliente cliente;

    @ManyToOne
    @JsonIgnoreProperties("ventas")
    private Vendedor vendedor;

    @Column(name="metodo_pago", nullable = false )
    @Enumerated(value = EnumType.STRING)
    private EnumMetodoPago metodoPago;


    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secuencia")
    @SequenceGenerator(name = "secuencia", allocationSize = 1)
    @Column(name = "numero_venta")
    private String numeroVenta;

    @Column(name = "estado_venta", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoVenta estadoVenta;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFecha() {
        return fecha;
    }

    public Venta fecha(Instant fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public Float getImporteTotal() {
        return importeTotal;
    }

    public Venta importeTotal(Float importeTotal) {
        this.importeTotal = importeTotal;
        return this;
    }



    public void setImporteTotal(Float importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Coche getCoche() {
        return coche;
    }

    public Venta coche(Coche coche) {
        this.coche = coche;
        return this;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Venta cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public Venta vendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
        return this;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Venta)) {
            return false;
        }
        return id != null && id.equals(((Venta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", importeTotal=" + getImporteTotal() +
            "}";
    }

    public EnumMetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(EnumMetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumeroVenta() {
        return numeroVenta;
    }

    public Venta setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
        return this;
    }

    public EnumEstadoVenta getEstadoVenta() {
        return estadoVenta;
    }

    public void setEstadoVenta(EnumEstadoVenta estadoVenta) {
        this.estadoVenta = estadoVenta;
    }
}

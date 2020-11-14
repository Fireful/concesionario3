package com.concesionario3.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * A Coche.
 */
@Entity
@Table(name = "moto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Moto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;


    @Column(name = "anio")
    private Integer anio;

    @Column(name = "electrico")
    private Boolean electrico;

    @Column(name = "color")
    private String color;

    @Column(name = "cilindrada")
    private Integer cilindrada;

    @Column(name = "precio")
    private Float precio;

    @OneToOne(mappedBy = "moto")
    @JsonIgnoreProperties(value = "moto")
    private Venta venta;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public Moto marca(String marca) {
        this.marca = marca;
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAnio() {
        return anio;
    }

    public Moto anio(Integer anio) {
        this.anio = anio;
        return this;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Boolean isElectrico() {
        return electrico;
    }

    public Moto electrico(Boolean electrico) {
        this.electrico = electrico;
        return this;
    }

    public void setElectrico(Boolean electrico) {
        this.electrico = electrico;
    }

    public Float getPrecio() {
        return precio;
    }

    public Moto precio(Float precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Moto)) {
            return false;
        }
        return id != null && id.equals(((Moto) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Moto{" +
            "id=" + getId() +
            ", marca='" + getMarca() + "'" +
            ", anio=" + getAnio() +
            ", electrico='" + isElectrico() + "'" +
            ", precio=" + getPrecio() + "'" +
            ", Venta id="+getVenta() + "'"+
            ", Color="+getColor() + "'"+
            "}";
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Moto color(String color) {
        this.color = color;
        return this;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

	public Moto modelo(String updatedModelo) {
		return null;
	}

    public Integer getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Integer cilindrada) {
        this.cilindrada = cilindrada;
    }
}

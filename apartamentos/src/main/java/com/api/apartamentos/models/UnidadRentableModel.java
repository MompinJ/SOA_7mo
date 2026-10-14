package com.api.apartamentos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="unidades_rentables")
public class UnidadRentableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUnidad;

    @ManyToOne
    @JoinColumn(name = "id_propiedad")
    private PropiedadModel propiedad;

    @Column
    private String nombre;

    @Column
    private String tipo;

    @Column
    private int capacidad;

    @Column
    private double precioNoche;


// Setters

    public void setIdUnidad(Long idUnidad) {
        this.idUnidad = idUnidad;
    }

    public void setPropiedad(PropiedadModel propiedad) {
        this.propiedad = propiedad;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }



// getters

    public Long getIdUnidad() {
        return idUnidad;
    }

    public PropiedadModel getPropiedad() {
        return propiedad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

}

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
@Table(name="propiedades")
public class PropiedadModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPropiedad;

    @ManyToOne
    @JoinColumn(name = "id_propietario")
    private ClienteModel propietario;

    @Column
    private String tipo;

    @Column
    private String titulo;

    @Column
    private String descripcion;

    @Column
    private String direccion;

    @Column
    private String ciudad;

    @Column
    private double precioNoche;

    @Column
    private int capacidad;

    @Column
    private String estado;


// Setters

    public void setIdPropiedad(Long idPropiedad) {
        this.idPropiedad = idPropiedad;
    }

    public void setPropietario(ClienteModel propietario) {
        this.propietario = propietario;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setPrecioNoche(double precioNoche) {
        this.precioNoche = precioNoche;
    }


    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

// getters

    public Long getIdPropiedad() {
        return idPropiedad;
    }

    public ClienteModel getPropietario() {
        return propietario;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getEstado() {
        return estado;
    }

}

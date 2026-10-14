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
@Table(name="propiedad_imagenes")
public class PropiedadImagenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagen;

    @ManyToOne
    @JoinColumn(name = "id_propiedad")
    private PropiedadModel propiedad;

    @Column
    private String url;

    @Column
    private String descripcion;


// Setters

    public void setIdImagen(Long idImagen) {
        this.idImagen = idImagen;
    }

    public void setPropiedad(PropiedadModel propiedad) {
        this.propiedad = propiedad;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


// getters

    public Long getIdImagen() {
        return idImagen;
    }

    public PropiedadModel getPropiedad() {
        return propiedad;
    }

    public String getUrl() {
        return url;
    }

    public String getDescripcion() {
        return descripcion;
    }

}

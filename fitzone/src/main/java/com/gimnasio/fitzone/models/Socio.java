package com.gimnasio.fitzone.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "socio")
public class Socio {

    @Id
    @Column(length = 8)
    private String numeroSocio;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(nullable = false)
    private LocalDate vencimientoMembresia;

    public Socio() {
    }

    public Socio(String numeroSocio, String nombre, String correo, LocalDate vencimientoMembresia) {
        this.numeroSocio = numeroSocio;
        this.nombre = nombre;
        this.correo = correo;
        this.vencimientoMembresia = vencimientoMembresia;
    }

    public String getNumeroSocio() { return numeroSocio; }
    public void setNumeroSocio(String numeroSocio) { this.numeroSocio = numeroSocio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public LocalDate getVencimientoMembresia() { return vencimientoMembresia; }
    public void setVencimientoMembresia(LocalDate vencimientoMembresia) { this.vencimientoMembresia = vencimientoMembresia; }
}

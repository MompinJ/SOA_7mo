package com.gimnasio.fitzone.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long folio;

    @ManyToOne
    @JoinColumn(name = "numero_socio", nullable = false)
    private Socio socio;

    @ManyToOne
    @JoinColumn(name = "id_clase", nullable = false)
    private Clase clase;

    @Column(nullable = false, length = 20)
    private String estatus;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public Inscripcion() {
    }

    public Inscripcion(Socio socio, Clase clase) {
        this.socio = socio;
        this.clase = clase;
        this.estatus = "CONFIRMADA";
        this.fecha = LocalDateTime.now();
    }

    public Long getFolio() { return folio; }

    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }

    public Clase getClase() { return clase; }
    public void setClase(Clase clase) { this.clase = clase; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}

package com.universidad.constancias.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "constancia")
public class Constancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matricula", nullable = false)
    private Estudiante estudiante;

    @ManyToOne
    @JoinColumn(name = "folio_pago", nullable = false)
    private Pago pago;

    @Column(nullable = false, length = 100)
    private String archivo;

    @Column(nullable = false)
    private LocalDate fecha;

    public Constancia() {
    }

    public Constancia(Estudiante estudiante, Pago pago, String archivo) {
        this.estudiante = estudiante;
        this.pago = pago;
        this.archivo = archivo;
        this.fecha = LocalDate.now();
    }

    public Long getId() { return id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    public String getArchivo() { return archivo; }
    public void setArchivo(String archivo) { this.archivo = archivo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}

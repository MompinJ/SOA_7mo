package com.api.apartamentos.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="reservaciones")
public class ReservacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReservacion;

    @ManyToOne
    @JoinColumn(name = "id_unidad")
    private UnidadRentableModel unidad;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteModel cliente;

    @Column
    private LocalDate fechaEntrada;

    @Column
    private LocalDate fechaSalida;

    @Column
    private double importeTotal;

    @Column
    private String estado;

    @Column
    private String comentario;


// Setters

    public void setIdReservacion(Long idReservacion) {
        this.idReservacion = idReservacion;
    }

    public void setUnidad(UnidadRentableModel unidad) {
        this.unidad = unidad;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }
    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }


    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }



// getters

    public Long getIdReservacion() {
        return idReservacion;
    }

    public UnidadRentableModel getUnidad() {
        return unidad;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public String getEstado() {
        return estado;
    }

    public String getComentario() {
        return comentario;
    }

}

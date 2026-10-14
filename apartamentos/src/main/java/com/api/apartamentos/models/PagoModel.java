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
@Table(name="pagos")
public class PagoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "id_reservacion")
    private ReservacionModel reserva;

    @Column
    private Double importe;

    @Column
    private String metodo;

    @Column
    private String estado;

    @Column
    private LocalDate fecha;


// Setters

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public void setReserva(ReservacionModel reserva) {
        this.reserva = reserva;
    }


    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }


// getters

    public Long getIdPago() {
        return idPago;
    }

    public ReservacionModel getReserva() {
        return reserva;
    }

    public Double getImporte() {
        return importe;
    }

    public String getMetodo() {
        return metodo;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFecha() {
        return fecha;
    }

}

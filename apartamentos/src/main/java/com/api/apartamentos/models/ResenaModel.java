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
@Table(name="resenas")
public class ResenaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResena;

    @ManyToOne
    @JoinColumn(name = "id_reservacion")
    private ReservacionModel reserva;

    @Column
    private int calificacion;

    @Column
    private String comentario;


// Setters

    public void setIdResena(Long idResena) {
        this.idResena = idResena;
    }

    public void setReserva(ReservacionModel reserva) {
        this.reserva = reserva;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


// getters

    public Long getIdResena() {
        return idResena;
    }

    public ReservacionModel getReserva() {
        return reserva;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public String getComentario() {
        return comentario;
    }

}

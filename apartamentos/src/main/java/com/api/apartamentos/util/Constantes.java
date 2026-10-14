package com.api.apartamentos.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Constantes {

    public static final Map<String, Object> ERROR_NO_ENCONTRADO = Map.of(
            "status", HttpStatus.NOT_FOUND,
            "mensaje", "Este recurso no ha sido encontrado");

    public static final Map<String, Object> ERROR_DATOS_INCOMPLETOS = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "Faltan datos obligatorios");

    public static final Map<String, Object> ERROR_DATOS_INVALIDOS = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "Los datos enviados no son validos");
    public static final Map<String, Object> ERROR_REFERENCIA = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "El registro relacionado no existe");

    public static final Map<String, Object> ERROR_FECHAS = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "La fecha final debe ser despues de la fecha inicial");

    public static final Map<String, Object> ERROR_IMPORTE = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "El importe debe ser mayor a 0 y no pasar del total de la reservacion");

    public static final Map<String, Object> ERROR_NO_DISPONIBLE = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "La unidad ya tiene una reservacion confirmada");

    public static final Map<String, Object> ERROR_TIENE_RELACIONES = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "No se puede eliminar porque tiene registros relacionados");

    public static final Map<String, Object> ELIMINADO = Map.of(
            "status", HttpStatus.OK,
            "mensaje", "El recurso ha sido eliminado correctamente");


    public static ResponseEntity<Object> responder(Map<String, Object> estado) {
        HttpStatus status = (HttpStatus) estado.get("status");
        return ResponseEntity.status(status).body(estado);
    }

}

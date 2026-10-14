package com.api.libreria.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// Clase que contiene constantes, para estandar de errores y respuestas

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
            "mensaje", "El libro o el usuario indicado no existe");

    public static final Map<String, Object> ERROR_SIN_EJEMPLARES = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "No hay ejemplares disponibles de este libro");

    public static final Map<String, Object> ELIMINADO = Map.of(
            "status", HttpStatus.OK,
            "mensaje", "El recurso ha sido eliminado correctamente");


    public static ResponseEntity<Object> responder(Map<String, Object> estado) {
        HttpStatus status = (HttpStatus) estado.get("status");
        return ResponseEntity.status(status).body(estado);
    }

}

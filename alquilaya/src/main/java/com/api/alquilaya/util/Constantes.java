package com.api.alquilaya.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Constantes {

    public static final Map<String, Object> CREADO = Map.of(
            "status", HttpStatus.CREATED,
            "mensaje", "El recurso ha sido creado correctamente");

    public static final Map<String, Object> ACTUALIZADO = Map.of(
            "status", HttpStatus.OK,
            "mensaje", "El recurso ha sido actualizado correctamente");

    public static final Map<String, Object> ELIMINADO = Map.of(
            "status", HttpStatus.OK,
            "mensaje", "El recurso ha sido eliminado correctamente");

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

    public static final Map<String, Object> ERROR_DUPLICADO = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "Ya existe un registro con esos datos");

    public static final Map<String, Object> ERROR_FECHAS = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "La fecha de devolucion debe ser despues de la fecha de alquiler");

    public static final Map<String, Object> ERROR_NO_DISPONIBLE = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "El vehiculo ya esta alquilado");

    public static final Map<String, Object> ERROR_TIENE_RELACIONES = Map.of(
            "status", HttpStatus.BAD_REQUEST,
            "mensaje", "No se puede eliminar porque tiene registros relacionados");


    public static ResponseEntity<Object> responder(Map<String, Object> estado) {
        HttpStatus status = (HttpStatus) estado.get("status");
        return ResponseEntity.status(status).body(estado);
    }

    public static ResponseEntity<Object> responder(Map<String, Object> estado, Object datos) {
        HashMap<String, Object> respuesta = new HashMap<>(estado);
        respuesta.put("datos", datos);
        HttpStatus status = (HttpStatus) estado.get("status");
        return ResponseEntity.status(status).body(respuesta);
    }

}

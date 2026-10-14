package com.api.libreria.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.libreria.models.LibroModel;
import com.api.libreria.util.Constantes;
import com.api.libreria.services.LibroService;

@RestController
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;


    // obtener libros
    @GetMapping
    public ArrayList<LibroModel> obtenerLibros() {
        System.out.println("hay " + libroService.obtenerLibros().size() + " libros");
        return libroService.obtenerLibros();
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Object> actualizarLibro(@RequestBody LibroModel request, @PathVariable("isbn") Long isbn) {
        // Verificar existencia
        if(!libroService.existeLibro(isbn)){
            System.out.println("no existe libro con id " + isbn);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
// verificar datos del libro
        if(request.getTitulo()==null || request.getTitulo().trim().equals("")) {
            System.out.println("faltan datos del libro");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if(request.getAutor()==null || request.getAutor().trim().equals("")) {
            System.out.println("faltan datos");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if(request.getNumeroEjemplares() < 0) {
            System.out.println("datos del libro invalidos");
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }

        // si todo esta bn, se actualiza el libro
        LibroModel actualizado = libroService.actualizarLibro(request, isbn);
        return ResponseEntity.ok(actualizado);
    }

    // obtener libro por el id
    @GetMapping("/{isbn}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long isbn) {
        LibroModel libro = libroService.obtenerPorId(isbn);
        // Verificar existencia
        if (libro == null) {
            System.out.println("no existe libro con id " + isbn);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(libro);
    }


    @PostMapping
    public ResponseEntity<Object> guardarLibro(@RequestBody LibroModel libro) {

        // Validaciones
        if(libro.getTitulo() == null || libro.getTitulo().trim().equals("")) {
            System.out.println("faltan datos del libro");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if(libro.getAutor() == null || libro.getAutor().trim().equals(""))
        {
            System.out.println("----- faltan datos");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }

        boolean ejemplaresBien = true;
        if (libro.getNumeroEjemplares() < 0) {
            ejemplaresBien = false;
        }
        if (ejemplaresBien == false) {
            System.out.println("datos del libro invalidos");
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }


        LibroModel nuevo = libroService.guardarLibro(libro);
        System.out.println("----");
        System.out.println("se guardo el libro con id " + nuevo.getIsbn());
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }


    // eliminar
    @DeleteMapping("/{isbn}")
    public ResponseEntity<Object> eliminarLibro(@PathVariable Long isbn) {

        // Verificar existencia
        if (!libroService.existeLibro(isbn)) {
            System.out.println("no existe libro con id " + isbn);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }

        // se elimina
        libroService.eliminarLibro(isbn);
        return Constantes.responder(Constantes.ELIMINADO);
    }

    @GetMapping("/autor")
    public ArrayList<LibroModel> buscarPorAutor(@RequestParam String autor) {
        return libroService.buscarPorAutor(autor);
    }

    @GetMapping("/titulo")
    public ArrayList<LibroModel> buscarPorTitulo(@RequestParam String titulo) {
        return libroService.buscarPorTitulo(titulo);
    }

}

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
import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.LibroService;
import com.api.libreria.util.Constantes;
import com.api.libreria.services.PrestamoService;
import com.api.libreria.services.UsuarioService;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private LibroService libroService;

    @Autowired
    private UsuarioService usuarioService;


    @GetMapping
    public ArrayList<PrestamoModel> obtenerPrestamos() {
        return prestamoService.obtenerPrestamos();
    }

    @GetMapping("/usuario")
    public ArrayList<PrestamoModel> buscarPorUsuario(@RequestParam Long idUsuario) {
        return prestamoService.buscarPorUsuario(idUsuario);
    }



    @GetMapping("/{id}")
    public ResponseEntity<Object> obtenerPorId(@PathVariable Long id) {
        PrestamoModel p = prestamoService.obtenerPorId(id);
        // Verificar existencia
        if (p == null) {
            System.out.println("no existe prestamo con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizarPrestamo(@RequestBody PrestamoModel request, @PathVariable("id") Long id) {
        // Verificar existencia
        if(!prestamoService.existePrestamo(id)) {
            System.out.println("no existe prestamo con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }

        if(request.getIsbn()==null || !libroService.existeLibro(request.getIsbn()))
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        if(request.getIdUsuario()==null || !usuarioService.existeUsuario(request.getIdUsuario()))
            return Constantes.responder(Constantes.ERROR_REFERENCIA);

        PrestamoModel anterior = prestamoService.obtenerPorId(id);
        if (!request.getIsbn().equals(anterior.getIsbn())) {
            System.out.println("no se puede cambiar el libro de un prestamo");
            return Constantes.responder(Constantes.ERROR_DATOS_INVALIDOS);
        }
        LibroModel libro = libroService.obtenerPorId(anterior.getIsbn());
        boolean estabaDevuelto = false;
        boolean seDevuelve = false;
        if (anterior.getEstado() != null && anterior.getEstado().toLowerCase().equals("devuelto")) {
            estabaDevuelto = true;
        }
        if(request.getEstado()!=null && request.getEstado().toLowerCase().equals("devuelto")) {
            seDevuelve = true;
        }
        if (estabaDevuelto == false && seDevuelve == true) {
            libro.setNumeroEjemplares(libro.getNumeroEjemplares() + 1);
            libroService.guardarLibro(libro);
            System.out.println("se devolvio el libro, ahora hay " + libro.getNumeroEjemplares());
        }
        if (estabaDevuelto && !seDevuelve) {
            if (libro.getNumeroEjemplares() <= 0) {
                return Constantes.responder(Constantes.ERROR_SIN_EJEMPLARES);
            }
            libro.setNumeroEjemplares(libro.getNumeroEjemplares() - 1);
            libroService.guardarLibro(libro);
        }

        PrestamoModel actualizado = prestamoService.actualizarPrestamo(request, id);
        return ResponseEntity.ok(actualizado);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> eliminarPrestamo(@PathVariable Long id) {
        // Verificar existencia
        if (!prestamoService.existePrestamo(id)) {
            System.out.println("no existe prestamo con id " + id);
            return Constantes.responder(Constantes.ERROR_NO_ENCONTRADO);
        }
        PrestamoModel prestamo = prestamoService.obtenerPorId(id);
        if (prestamo.getEstado() == null || !prestamo.getEstado().toLowerCase().equals("devuelto")) {
            System.out.println("el prestamo seguia activo, se regresa el ejemplar");
            LibroModel libro = libroService.obtenerPorId(prestamo.getIsbn());
            if (libro != null) {
                libro.setNumeroEjemplares(libro.getNumeroEjemplares() + 1);
                libroService.guardarLibro(libro);
            }
        }
        prestamoService.eliminarPrestamo(id);

        return Constantes.responder(Constantes.ELIMINADO);
    }

    @PostMapping
    public ResponseEntity<Object> guardarPrestamo(@RequestBody PrestamoModel prestamo) {

        if (prestamo.getIsbn() == null) {
            System.out.println("faltan datos del prestamo");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if (prestamo.getIdUsuario() == null) {
            System.out.println("faltan datos");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }
        if(prestamo.getFechaPrestamo()==null || prestamo.getFechaPrestamo().trim().equals("")) {
            System.out.println("faltan datos del prestamo");
            return Constantes.responder(Constantes.ERROR_DATOS_INCOMPLETOS);
        }

        // Verificar existencia
        if (libroService.existeLibro(prestamo.getIsbn()) == false) {
            System.out.println("el libro o el usuario no existe");
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }
        if (!usuarioService.existeUsuario(prestamo.getIdUsuario()))
        {
            System.out.println("el libro o el usuario no existe");
            return Constantes.responder(Constantes.ERROR_REFERENCIA);
        }

        LibroModel libro = libroService.obtenerPorId(prestamo.getIsbn());
        if (libro.getNumeroEjemplares() <= 0) {
            System.out.println("no quedan ejemplares de " + libro.getTitulo());
            return Constantes.responder(Constantes.ERROR_SIN_EJEMPLARES);
        }
        int ejemplares = libro.getNumeroEjemplares();
        ejemplares = ejemplares - 1;
        libro.setNumeroEjemplares(ejemplares);
        libroService.guardarLibro(libro);
        System.out.println("quedan " + ejemplares + " ejemplares");

        PrestamoModel nuevo = prestamoService.guardarPrestamo(prestamo);
        System.out.println("----");
        System.out.println("se guardo el prestamo con id " + nuevo.getId());
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping("/libro")
    public ArrayList<PrestamoModel> buscarPorLibro(@RequestParam Long isbn) {
        return prestamoService.buscarPorLibro(isbn);
    }



}

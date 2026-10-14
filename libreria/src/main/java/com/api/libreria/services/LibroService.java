package com.api.libreria.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.LibroModel;
import com.api.libreria.repositories.ILibroRepository;

@Service
public class LibroService {

    @Autowired
    ILibroRepository libroRepository;


    public ArrayList<LibroModel> obtenerLibros() {
        return (ArrayList<LibroModel>) libroRepository.findAll();
    }



    public LibroModel guardarLibro(LibroModel libro) {
        return libroRepository.save(libro);
    }

    public LibroModel obtenerPorId(Long id) {
        return libroRepository.findById(id).orElse(null);
    }



    public boolean existeLibro(Long isbn){
        LibroModel libro = obtenerPorId(isbn);
        if (libro == null) {
            return false;
        }
        return true;
    }
    public ArrayList<LibroModel> buscarPorTitulo(String autor) {
        return libroRepository.findByTitulo(autor);
    }


    public boolean eliminarLibro(Long isbn) {
        // Verificar existencia
        if(!libroRepository.existsById(isbn)) {
            System.out.println("no existe libro " + isbn);
            return false;

        }
        libroRepository.deleteById(isbn);
        return true;
    }

    public ArrayList<LibroModel> buscarPorAutor(String autor) {
        return libroRepository.findByAutor(autor);
    }


    public LibroModel actualizarLibro(LibroModel request, Long isbn) {
        LibroModel libro = libroRepository.findById(isbn).orElse(null);

        // Verificar existencia
        if(libro == null) {
            System.out.println("no se encontro libro " + isbn);
            return null;
        }

        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setAnioPublicacion(request.getAnioPublicacion());
        libro.setNumeroEjemplares(request.getNumeroEjemplares());
        return libroRepository.save(libro);
    }
}

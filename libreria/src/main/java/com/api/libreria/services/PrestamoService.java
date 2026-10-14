package com.api.libreria.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.libreria.models.PrestamoModel;
import com.api.libreria.repositories.IPrestamoRepository;

@Service
public class PrestamoService {

    @Autowired
    IPrestamoRepository prestamoRepository;

    public ArrayList<PrestamoModel> obtenerPrestamos() {
        return (ArrayList<PrestamoModel>) prestamoRepository.findAll();
    }
    public PrestamoModel obtenerPorId(Long id) {
        return prestamoRepository.findById(id).orElse(null);
    }

    public PrestamoModel guardarPrestamo(PrestamoModel prestamo) {

        if(prestamo.getEstado() == null || prestamo.getEstado().equals("")) {
            prestamo.setEstado("ACTIVO");
            System.out.println("no mandaron estado, se pone ACTIVO");
        }
        return prestamoRepository.save(prestamo);
    }
    public boolean existePrestamo(Long id) {
        return prestamoRepository.existsById(id);
    }



    public PrestamoModel actualizarPrestamo(PrestamoModel request, Long id) {
        PrestamoModel prestamo = prestamoRepository.findById(id).orElse(null);
        // Verificar existencia
        if (prestamo == null) {
            return null;
        }


        prestamo.setIsbn(request.getIsbn());
        prestamo.setIdUsuario(request.getIdUsuario());
        prestamo.setFechaPrestamo(request.getFechaPrestamo());
        prestamo.setFechaDevolucion(request.getFechaDevolucion());
        prestamo.setEstado(request.getEstado());
        return prestamoRepository.save(prestamo);
    }


    public boolean eliminarPrestamo(Long id) {
        // Verificar existencia
        if (!prestamoRepository.existsById(id)) {
            System.out.println("no existe prestamo " + id);
            return false;
        }
        prestamoRepository.deleteById(id);
        return true;
    }
    public ArrayList<PrestamoModel> buscarPorUsuario(Long idUsuario) {
        ArrayList<PrestamoModel> todos = obtenerPrestamos();
        ArrayList<PrestamoModel> delUsuario = new ArrayList<>();
        for (int i = 0; i < todos.size(); i++) {
            if (todos.get(i).getIdUsuario().equals(idUsuario)) {
                delUsuario.add(todos.get(i));
            }
        }
        return delUsuario;
    }

    public ArrayList<PrestamoModel> buscarPorLibro(Long isbn) {
        return prestamoRepository.findByIsbn(isbn);
    }
}

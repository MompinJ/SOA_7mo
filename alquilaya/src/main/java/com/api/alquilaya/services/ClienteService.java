package com.api.alquilaya.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.repositories.IAlquilerRepository;
import com.api.alquilaya.repositories.IClienteRepository;

@Service 
public class ClienteService {

    @Autowired
    IClienteRepository clienteRepository;
    @Autowired
    IAlquilerRepository alquilerRepository;



    public ArrayList<ClienteModel> obtenerClientes() {
        return (ArrayList<ClienteModel>) clienteRepository.findAll();
    }

    public ClienteModel guardarCliente(ClienteModel cliente) {
        return clienteRepository.save(cliente);
    }

    public ClienteModel obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public boolean existeCliente(Long id) {
        ClienteModel cliente = obtenerPorId(id);
        if (cliente != null) {
            return true;
        } else {
            return false;
        }
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public ClienteModel buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public ClienteModel buscarPorLicencia(String licencia) {
        return clienteRepository.findByLicenciaConducir(licencia);
    }

    public boolean tieneAlquileres(Long id) {
        return alquilerRepository.existsByClienteIdCliente(id);
    }

}

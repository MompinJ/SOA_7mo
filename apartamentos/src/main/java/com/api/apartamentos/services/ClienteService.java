package com.api.apartamentos.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.apartamentos.models.ClienteModel;
import com.api.apartamentos.repositories.IClienteRepository;
import com.api.apartamentos.repositories.IReservacionRepository;
import com.api.apartamentos.repositories.IPropiedadRepository;

@Service
public class ClienteService {

    @Autowired
    IClienteRepository clienteRepository;
    @Autowired
    IReservacionRepository reservacionRepository;
    @Autowired
    IPropiedadRepository propiedadRepository;

    public ArrayList<ClienteModel> obtenerClientes() {
        return (ArrayList<ClienteModel>) clienteRepository.findAll();
    }

    public ClienteModel guardarCliente(ClienteModel cliente) {
        if (cliente.getTipo() == null || cliente.getTipo().equals("")) {
            cliente.setTipo("CLIENTE");
        }
        return clienteRepository.save(cliente);
    }
    public ClienteModel obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }


    public ClienteModel actualizarCliente(ClienteModel request, Long id) {
        ClienteModel cliente = clienteRepository.findById(id).orElse(null);
        if (cliente == null) {
            return null;
        }
        cliente.setNombres(request.getNombres());
        cliente.setApellidos(request.getApellidos());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setTipo(request.getTipo());
        return clienteRepository.save(cliente);
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public boolean tieneRelaciones(Long id) {
        if (reservacionRepository.existsByClienteId(id) || propiedadRepository.existsByPropietarioId(id)) {
            return true;
        }
        return false;
    }
}

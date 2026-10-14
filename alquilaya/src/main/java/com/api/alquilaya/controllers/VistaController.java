package com.api.alquilaya.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.AlquilerService;
import com.api.alquilaya.services.ClienteService;
import com.api.alquilaya.services.OficinaAlquilerService;
import com.api.alquilaya.services.VehiculoService;

@Controller
public class VistaController {

    @Autowired
    private VehiculoService vehiculoService;
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private OficinaAlquilerService oficinaService;
    @Autowired
    private AlquilerService alquilerService;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/vehiculos";
    }

    @GetMapping("/vehiculos")
    public String vehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoService.obtenerVehiculos());
        model.addAttribute("oficinas", oficinaService.obtenerOficinas());
        model.addAttribute("vehiculo", new VehiculoModel());
        return "vehiculos";
    }

    @GetMapping("/vehiculos/{id}/editar")
    public String editarVehiculo(@PathVariable Long id, Model model, RedirectAttributes ra) {
        VehiculoModel vehiculo = vehiculoService.obtenerPorId(id);
        if(vehiculo == null){
            System.out.println("no existe vehiculo con id " + id);
            ra.addFlashAttribute("error", "Ese vehiculo no existe");
            return "redirect:/vehiculos";
        }
        model.addAttribute("vehiculos", vehiculoService.obtenerVehiculos());
        model.addAttribute("oficinas", oficinaService.obtenerOficinas());
        model.addAttribute("vehiculo", vehiculo);
        return "vehiculos";
    }

    @PostMapping("/vehiculos/guardar")
    public String guardarVehiculo(VehiculoModel vehiculo, RedirectAttributes ra) {
        if (vehiculoService.buscarPorMatricula(vehiculo.getMatricula()) != null) {
            System.out.println("la matricula " + vehiculo.getMatricula() + " ya esta registrada");
            ra.addFlashAttribute("error", "Ya hay un vehiculo con la matricula " + vehiculo.getMatricula());
            return "redirect:/vehiculos";
        }
        OficinaAlquilerModel oficina = oficinaService.obtenerPorId(vehiculo.getOficina().getIdOficina());
        if (oficina == null) {
            ra.addFlashAttribute("error", "La oficina no existe");
            return "redirect:/vehiculos";
        }
        vehiculo.setOficina(oficina);
        vehiculoService.guardarVehiculo(vehiculo);
        ra.addFlashAttribute("ok", "Se guardo el vehiculo " + vehiculo.getMatricula());
        return "redirect:/vehiculos";
    }

    @PostMapping("/vehiculos/{id}/actualizar")
    public String actualizarVehiculo(@PathVariable Long id, VehiculoModel request, RedirectAttributes ra) {
        VehiculoModel vehiculo = vehiculoService.obtenerPorId(id);
        if (vehiculo == null) {
            return "redirect:/vehiculos";
        }
        VehiculoModel otro = vehiculoService.buscarPorMatricula(request.getMatricula());
        if (otro != null && !otro.getIdVehiculo().equals(id)) {
            System.out.println("la matricula " + request.getMatricula() + " ya la tiene otro vehiculo");
            ra.addFlashAttribute("error", "La matricula " + request.getMatricula() + " ya la tiene otro vehiculo");
            return "redirect:/vehiculos/" + id + "/editar";
        }
        OficinaAlquilerModel oficina = oficinaService.obtenerPorId(request.getOficina().getIdOficina());
        if (oficina == null) {
            ra.addFlashAttribute("error", "La oficina no existe");
            return "redirect:/vehiculos/" + id + "/editar";
        }
        request.setIdVehiculo(id);
        request.setOficina(oficina);
        request.setEstado(vehiculo.getEstado());
        vehiculoService.guardarVehiculo(request);
        ra.addFlashAttribute("ok", "Se actualizo el vehiculo " + request.getMatricula());
        return "redirect:/vehiculos";
    }

    @PostMapping("/vehiculos/{id}/eliminar")
    public String eliminarVehiculo(@PathVariable Long id, RedirectAttributes ra) {
        if (vehiculoService.tieneAlquileres(id)) {
            ra.addFlashAttribute("error", "No se puede borrar, el vehiculo tiene alquileres");
            return "redirect:/vehiculos";
        }
        vehiculoService.eliminarVehiculo(id);
        ra.addFlashAttribute("ok", "Vehiculo eliminado");
        return "redirect:/vehiculos";
    }

    @GetMapping("/clientes")
    public String clientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerClientes());
        model.addAttribute("cliente", new ClienteModel());
        return "clientes";
    }

    @GetMapping("/clientes/{id}/editar")
    public String editarCliente(@PathVariable Long id, Model model, RedirectAttributes ra) {
        ClienteModel cliente = clienteService.obtenerPorId(id);
        if (cliente == null) {
            ra.addFlashAttribute("error", "Ese cliente no existe");
            return "redirect:/clientes";
        }
        model.addAttribute("clientes", clienteService.obtenerClientes());
        model.addAttribute("cliente", clienteService.obtenerPorId(id));
        return "clientes";
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(ClienteModel cliente, RedirectAttributes ra) {
        if (clienteService.buscarPorEmail(cliente.getEmail()) != null || clienteService.buscarPorLicencia(cliente.getLicenciaConducir()) != null) {
            System.out.println("ya hay un cliente con ese correo o licencia");
            ra.addFlashAttribute("error", "Ya existe un cliente con ese correo o licencia");
            return "redirect:/clientes";
        }
        clienteService.guardarCliente(cliente);
        ra.addFlashAttribute("ok", "Se guardo a " + cliente.getNombre());
        return "redirect:/clientes";
    }

    @PostMapping("/clientes/{id}/actualizar")
    public String actualizarCliente(@PathVariable Long id, ClienteModel request, RedirectAttributes ra) {
        if (clienteService.obtenerPorId(id) == null) {
            return "redirect:/clientes";
        }
        ClienteModel otro = clienteService.buscarPorEmail(request.getEmail());
        ClienteModel otro2 = clienteService.buscarPorLicencia(request.getLicenciaConducir());
        if ((otro != null && !otro.getIdCliente().equals(id)) || (otro2 != null && !otro2.getIdCliente().equals(id))) {
            System.out.println("el correo o la licencia ya los tiene otro cliente");
            ra.addFlashAttribute("error", "El correo o la licencia ya los tiene otro cliente");
            return "redirect:/clientes/" + id + "/editar";
        }
        request.setIdCliente(id);
        clienteService.guardarCliente(request);
        ra.addFlashAttribute("ok", "Se actualizo a " + request.getNombre());
        return "redirect:/clientes";
    }

    @PostMapping("/clientes/{id}/eliminar")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes ra) {
        if (clienteService.tieneAlquileres(id) == true) {
            ra.addFlashAttribute("error", "No se puede borrar, el cliente tiene alquileres");
        } else {
            clienteService.eliminarCliente(id);
            ra.addFlashAttribute("ok", "Cliente eliminado");
        }
        return "redirect:/clientes";
    }

    @GetMapping("/oficinas")
    public String oficinas(Model model) {
        model.addAttribute("oficinas", oficinaService.obtenerOficinas());
        model.addAttribute("oficina", new OficinaAlquilerModel());
        return "oficinas";
    }

    @GetMapping("/oficinas/{id}/editar")
    public String editarOficina(@PathVariable Long id, Model model, RedirectAttributes ra) {
        OficinaAlquilerModel oficina = oficinaService.obtenerPorId(id);
        if (oficina == null) {
            ra.addFlashAttribute("error", "Esa oficina no existe");
            return "redirect:/oficinas";
        }
        model.addAttribute("oficinas", oficinaService.obtenerOficinas());
        model.addAttribute("oficina", oficina);
        return "oficinas";
    }

    @PostMapping("/oficinas/guardar")
    public String guardarOficina(OficinaAlquilerModel oficina, RedirectAttributes ra) {
        oficinaService.guardarOficina(oficina);
        System.out.println("----");
        System.out.println("se guardo la oficina " + oficina.getNombre());
        ra.addFlashAttribute("ok", "Se guardo la oficina " + oficina.getNombre());
        return "redirect:/oficinas";
    }

    @PostMapping("/oficinas/{id}/actualizar")
    public String actualizarOficina(@PathVariable Long id, OficinaAlquilerModel request, RedirectAttributes ra) {
        if (oficinaService.obtenerPorId(id) == null) {
            return "redirect:/oficinas";
        }
        request.setIdOficina(id);
        oficinaService.guardarOficina(request);
        ra.addFlashAttribute("ok", "Se actualizo la oficina " + request.getNombre());
        return "redirect:/oficinas";
    }

    @PostMapping("/oficinas/{id}/eliminar")
    public String eliminarOficina(@PathVariable Long id, RedirectAttributes ra) {
        if (oficinaService.tieneVehiculos(id)) {
            System.out.println("la oficina tiene vehiculos");
            ra.addFlashAttribute("error", "No se puede borrar, la oficina tiene vehiculos");
            return "redirect:/oficinas";
        }
        oficinaService.eliminarOficina(id);
        ra.addFlashAttribute("ok", "Oficina eliminada");
        return "redirect:/oficinas";
    }

    @GetMapping("/alquileres")
    public String alquileres(Model model) {
        model.addAttribute("alquileres", alquilerService.obtenerAlquileres());
        model.addAttribute("clientes", clienteService.obtenerClientes());
        ArrayList<VehiculoModel> disponibles = vehiculoService.buscarPorEstado("DISPONIBLE");
        System.out.println("vehiculos disponibles: " + disponibles.size());
        model.addAttribute("disponibles", disponibles);
        model.addAttribute("alquiler", new AlquilerModel());
        return "alquileres";
    }

    @GetMapping("/alquileres/{id}/editar")
    public String editarAlquiler(@PathVariable Long id, Model model, RedirectAttributes ra) {
        AlquilerModel alquiler = alquilerService.obtenerPorId(id);
        if (alquiler == null) {
            ra.addFlashAttribute("error", "Ese alquiler no existe");
            return "redirect:/alquileres";
        }
        model.addAttribute("alquileres", alquilerService.obtenerAlquileres());
        model.addAttribute("alquiler", alquiler);
        return "alquileres";
    }

    @PostMapping("/alquileres/guardar")
    public String guardarAlquiler(AlquilerModel alquiler, RedirectAttributes ra) {
        if (!alquiler.getFechaDevolucion().isAfter(alquiler.getFechaAlquiler())) {
            System.out.println("las fechas estan mal");
            ra.addFlashAttribute("error", "La fecha de devolucion debe ser despues de la fecha de alquiler");
            return "redirect:/alquileres";
        }
        VehiculoModel vehiculo = vehiculoService.obtenerPorId(alquiler.getVehiculo().getIdVehiculo());
        ClienteModel cliente = clienteService.obtenerPorId(alquiler.getCliente().getIdCliente());
        if (vehiculo == null || cliente == null || vehiculo.getEstado().equals("ALQUILADO")) {
            ra.addFlashAttribute("error", "El vehiculo no esta disponible");
            return "redirect:/alquileres";
        }
        vehiculo.setEstado("ALQUILADO");
        vehiculoService.guardarVehiculo(vehiculo);
        alquiler.setVehiculo(vehiculo);
        alquiler.setCliente(cliente);
        alquilerService.guardarAlquiler(alquiler);
        ra.addFlashAttribute("ok", "Alquiler registrado, " + vehiculo.getMatricula() + " ahora esta ALQUILADO");
        return "redirect:/alquileres";
    }

    @PostMapping("/alquileres/{id}/actualizar")
    public String actualizarAlquiler(@PathVariable Long id, AlquilerModel request, RedirectAttributes ra) {
        AlquilerModel alquiler = alquilerService.obtenerPorId(id);
        if (alquiler == null) {
            return "redirect:/alquileres";
        }
        if (!request.getFechaDevolucion().isAfter(request.getFechaAlquiler())) {
            System.out.println("las fechas estan mal");
            ra.addFlashAttribute("error", "La fecha de devolucion debe ser despues de la fecha de alquiler");
            return "redirect:/alquileres/" + id + "/editar";
        }
        alquiler.setFechaAlquiler(request.getFechaAlquiler());
        alquiler.setFechaDevolucion(request.getFechaDevolucion());
        alquiler.setPrecioTotal(request.getPrecioTotal());
        System.out.println("nuevo precio del alquiler " + id + ": " + request.getPrecioTotal());
        alquilerService.guardarAlquiler(alquiler);
        ra.addFlashAttribute("ok", "Se actualizo el alquiler #" + id);
        return "redirect:/alquileres";
    }

    @PostMapping("/alquileres/{id}/devolver")
    public String devolver(@PathVariable Long id, RedirectAttributes ra) {
        AlquilerModel alquiler = alquilerService.obtenerPorId(id);
        if (alquiler == null) {
            System.out.println("no existe el alquiler " + id);
            return "redirect:/alquileres";
        }
        VehiculoModel vehiculo = alquiler.getVehiculo();
        vehiculo.setEstado("DISPONIBLE");
        vehiculoService.guardarVehiculo(vehiculo);
        alquilerService.eliminarAlquiler(id);
        System.out.println("se devolvio el vehiculo " + vehiculo.getMatricula());
        ra.addFlashAttribute("ok", "Se devolvio el " + vehiculo.getMarca() + " " + vehiculo.getModelo());
        return "redirect:/alquileres";
    }
}

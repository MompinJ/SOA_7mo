package com.universidad.constancias.services;

import com.universidad.constancias.models.Estudiante;
import com.universidad.constancias.models.Pago;
import com.universidad.constancias.repositories.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    private static final int DIAS_VIGENCIA = 3;

    private final PagoRepository repositorio;

    public PagoService(PagoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Pago> listar() {
        return repositorio.findAll();
    }

    public Pago cobrarTramite(Estudiante estudiante, String concepto, double monto) {
        Pago nuevo = new Pago(estudiante, concepto, monto);
        nuevo.setEstatus("CONFIRMADO");
        nuevo.setFechaPago(LocalDate.now());
        Pago pago = repositorio.save(nuevo);
        log.info("[Pago] Cobro de ${} a {} por {} (folio {})", monto, estudiante.getMatricula(), concepto, pago.getFolio());
        return pago;
    }

    public Pago generarReferencia(Estudiante estudiante, String concepto, double monto) {
        Pago pago = new Pago(estudiante, concepto, monto);
        pago.setReferencia("REF" + (100000000 + new Random().nextInt(900000000)));
        pago.setEstatus("PENDIENTE");
        log.info("[Pago] Referencia {} generada para {} por ${}", pago.getReferencia(), estudiante.getMatricula(), monto);
        return repositorio.save(pago);
    }

    public Pago consultarPorReferencia(String referencia) {
        return repositorio.findByReferencia(referencia);
    }

    public boolean referenciaVigente(Pago pago) {
        return !LocalDate.now().isAfter(pago.getFecha().plusDays(DIAS_VIGENCIA));
    }

    public Pago registrarPago(Pago pago, String metodoPago, LocalDate fechaPago) {
        pago.setMetodoPago(metodoPago);
        pago.setFechaPago(fechaPago);
        pago.setEstatus("CONFIRMADO");
        log.info("[Pago] Pago registrado de la referencia {} (folio {})", pago.getReferencia(), pago.getFolio());
        return repositorio.save(pago);
    }
}

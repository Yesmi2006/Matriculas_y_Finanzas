package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.HistorialEstadoPagoResponse;
import pe.edu.upeu.lamb.finanzas.dto.PagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.PagoResponse;
import pe.edu.upeu.lamb.finanzas.dto.PagoUpdateRequest;
import pe.edu.upeu.lamb.finanzas.dto.RechazarPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.ReembolsarPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.ResumenPagoResponse;
import pe.edu.upeu.lamb.finanzas.dto.TransaccionPasarelaResponse;
import pe.edu.upeu.lamb.finanzas.entity.ConceptoPago;
import pe.edu.upeu.lamb.finanzas.entity.EstadoPago;
import pe.edu.upeu.lamb.finanzas.entity.EstadoTransaccionPasarela;
import pe.edu.upeu.lamb.finanzas.entity.MotivoPago;
import pe.edu.upeu.lamb.finanzas.entity.Pago;
import pe.edu.upeu.lamb.finanzas.entity.TipoMotivoPago;
import pe.edu.upeu.lamb.finanzas.event.MatriculaConfirmadaEvento;
import pe.edu.upeu.lamb.finanzas.exception.BusinessRuleException;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.PagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.ConceptoPagoRepository;
import pe.edu.upeu.lamb.finanzas.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    private static final String CODIGO_CONCEPTO_MATRICULA = "MATRICULA";

    // Fallback por si el concepto MATRICULA no esta configurado (o fue desactivado):
    // evita que se caiga la generacion automatica del cargo ante un evento matricula.confirmada.
    private static final BigDecimal MONTO_MATRICULA_PLACEHOLDER = new BigDecimal("350.00");

    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;
    private final ConceptoPagoRepository conceptoPagoRepository;
    private final HistorialEstadoPagoService historialEstadoPagoService;
    private final TransaccionPasarelaService transaccionPasarelaService;
    private final MotivoPagoService motivoPagoService;

    /**
     * Idempotente por matriculaId: si el evento matricula.confirmada se reentrega, no genera
     * un segundo cargo (constraint unica en matricula_id respalda esto a nivel de BD tambien).
     */
    @Transactional
    public void generarCargoDeMatricula(MatriculaConfirmadaEvento evento) {
        if (pagoRepository.findByMatriculaId(evento.getMatriculaId()).isPresent()) {
            log.warn("component=processor matriculaId={} status=ignored motivo=\"cargo ya generado\"",
                    evento.getMatriculaId());
            return;
        }
        BigDecimal monto = conceptoPagoRepository.findByCodigo(CODIGO_CONCEPTO_MATRICULA)
                .filter(ConceptoPago::isVigente)
                .map(ConceptoPago::getMonto)
                .orElseGet(() -> {
                    log.warn("component=processor concepto={} status=fallback motivo=\"concepto no configurado o no vigente\"",
                            CODIGO_CONCEPTO_MATRICULA);
                    return MONTO_MATRICULA_PLACEHOLDER;
                });
        Pago pago = Pago.builder()
                .idPersona(evento.getIdPersona())
                .matriculaId(evento.getMatriculaId())
                .concepto("Matricula " + evento.getPeriodo())
                .monto(monto)
                .estado(EstadoPago.PENDIENTE)
                .fechaPago(Instant.now())
                .build();
        Pago guardado = pagoRepository.save(pago);
        historialEstadoPagoService.registrar(guardado.getId(), null, EstadoPago.PENDIENTE);
        log.info("component=processor matriculaId={} idPersona={} pagoId={} estado={} status=processed",
                evento.getMatriculaId(), evento.getIdPersona(), guardado.getId(), guardado.getEstado());
    }

    public List<PagoResponse> listar(Long idPersona, EstadoPago estado, Long matriculaId) {
        Specification<Pago> spec = Specification.where((Specification<Pago>) null);
        if (idPersona != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("idPersona"), idPersona));
        }
        if (estado != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("estado"), estado));
        }
        if (matriculaId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("matriculaId"), matriculaId));
        }
        return pagoRepository.findAll(spec).stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    public ResumenPagoResponse resumen(Long idPersona) {
        List<Pago> pagos = pagoRepository.findByIdPersona(idPersona);
        return ResumenPagoResponse.builder()
                .idPersona(idPersona)
                .totalPendiente(sumarPorEstado(pagos, EstadoPago.PENDIENTE))
                .totalAprobado(sumarPorEstado(pagos, EstadoPago.APROBADO))
                .totalReembolsado(sumarPorEstado(pagos, EstadoPago.REEMBOLSADO))
                .build();
    }

    private BigDecimal sumarPorEstado(List<Pago> pagos, EstadoPago estado) {
        return pagos.stream()
                .filter(p -> p.getEstado() == estado)
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public PagoResponse obtener(Long id) {
        return pagoMapper.toResponse(buscarOFallar(id));
    }

    public List<HistorialEstadoPagoResponse> historial(Long id) {
        buscarOFallar(id);
        return historialEstadoPagoService.listarPorPago(id);
    }

    public List<TransaccionPasarelaResponse> transacciones(Long id) {
        buscarOFallar(id);
        return transaccionPasarelaService.listarPorPago(id);
    }

    @Transactional
    public PagoResponse procesar(PagoRequest request) {
        Pago pago = pagoMapper.toEntity(request);
        pago.setEstado(EstadoPago.APROBADO);
        pago.setReferenciaExterna(UUID.randomUUID().toString());
        Pago guardado = pagoRepository.save(pago);
        historialEstadoPagoService.registrar(guardado.getId(), null, EstadoPago.APROBADO);
        transaccionPasarelaService.registrar(guardado.getId(), guardado.getMonto(), EstadoTransaccionPasarela.EXITOSA);
        return pagoMapper.toResponse(guardado);
    }

    /**
     * Solo se puede corregir el metodo mientras el pago sigue PENDIENTE (p.ej. el cargo
     * automatico que genera un evento de matricula nace sin metodo elegido todavia).
     */
    public PagoResponse actualizar(Long id, PagoUpdateRequest request) {
        Pago pago = buscarOFallar(id);
        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new BusinessRuleException("Solo se puede editar un pago PENDIENTE (id=" + id
                    + ", estado actual=" + pago.getEstado() + ")");
        }
        pago.setMetodo(request.getMetodo());
        return pagoMapper.toResponse(pagoRepository.save(pago));
    }

    /**
     * Paso separado de procesar(): aprueba un cargo que ya existe en PENDIENTE (p.ej. el que
     * genera automaticamente el evento matricula.confirmada) una vez que se eligio metodo de pago.
     */
    @Transactional
    public PagoResponse aprobar(Long id) {
        Pago pago = buscarOFallar(id);
        if (pago.getEstado() == EstadoPago.APROBADO) {
            log.warn("component=processor pagoId={} status=ignored motivo=\"ya estaba aprobado\"", id);
            return pagoMapper.toResponse(pago);
        }
        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new BusinessRuleException("No se puede aprobar el pago " + id
                    + ": esta en estado " + pago.getEstado() + ", se esperaba PENDIENTE");
        }
        if (pago.getMetodo() == null) {
            throw new BusinessRuleException("No se puede aprobar el pago " + id + " sin metodo de pago asignado");
        }
        EstadoPago estadoAnterior = pago.getEstado();
        pago.setEstado(EstadoPago.APROBADO);
        pago.setReferenciaExterna(UUID.randomUUID().toString());
        Pago guardado = pagoRepository.save(pago);
        historialEstadoPagoService.registrar(id, estadoAnterior, EstadoPago.APROBADO);
        transaccionPasarelaService.registrar(id, guardado.getMonto(), EstadoTransaccionPasarela.EXITOSA);
        return pagoMapper.toResponse(guardado);
    }

    @Transactional
    public PagoResponse rechazar(Long id, RechazarPagoRequest request) {
        Pago pago = buscarOFallar(id);
        if (pago.getEstado() != EstadoPago.PENDIENTE) {
            throw new BusinessRuleException("No se puede rechazar el pago " + id
                    + ": esta en estado " + pago.getEstado() + ", se esperaba PENDIENTE");
        }
        Long idMotivoPago = request != null ? request.getIdMotivoPago() : null;
        if (idMotivoPago != null) {
            validarMotivo(idMotivoPago, TipoMotivoPago.RECHAZO);
            pago.setIdMotivoPago(idMotivoPago);
        }
        EstadoPago estadoAnterior = pago.getEstado();
        pago.setEstado(EstadoPago.RECHAZADO);
        Pago guardado = pagoRepository.save(pago);
        historialEstadoPagoService.registrar(id, estadoAnterior, EstadoPago.RECHAZADO);
        transaccionPasarelaService.registrar(id, guardado.getMonto(), EstadoTransaccionPasarela.FALLIDA);
        return pagoMapper.toResponse(guardado);
    }

    @Transactional
    public PagoResponse reembolsar(Long id, ReembolsarPagoRequest request) {
        Pago pago = buscarOFallar(id);
        Long idMotivoPago = request != null ? request.getIdMotivoPago() : null;
        if (idMotivoPago != null) {
            validarMotivo(idMotivoPago, TipoMotivoPago.REEMBOLSO);
            pago.setIdMotivoPago(idMotivoPago);
        }
        EstadoPago estadoAnterior = pago.getEstado();
        pago.setEstado(EstadoPago.REEMBOLSADO);
        Pago guardado = pagoRepository.save(pago);
        historialEstadoPagoService.registrar(id, estadoAnterior, EstadoPago.REEMBOLSADO);
        return pagoMapper.toResponse(guardado);
    }

    private void validarMotivo(Long idMotivoPago, TipoMotivoPago tipoEsperado) {
        MotivoPago motivo = motivoPagoService.buscarOFallar(idMotivoPago);
        if (motivo.getTipo() != tipoEsperado) {
            throw new BusinessRuleException("El motivo " + idMotivoPago + " no es de tipo " + tipoEsperado);
        }
    }

    Pago buscarOFallar(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado: " + id));
    }
}

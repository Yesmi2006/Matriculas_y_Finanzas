package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.AnularMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.CambiarSeccionRequest;
import pe.edu.upeu.lamb.matricula.dto.ConstanciaMatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.DetalleMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.DetalleMatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.HistorialEstadoMatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.MatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.MatriculaResponse;
import pe.edu.upeu.lamb.matricula.dto.MatriculaUpdateRequest;
import pe.edu.upeu.lamb.matricula.entity.DetalleMatricula;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import pe.edu.upeu.lamb.matricula.entity.EstadoSeccion;
import pe.edu.upeu.lamb.matricula.entity.Matricula;
import pe.edu.upeu.lamb.matricula.entity.Seccion;
import pe.edu.upeu.lamb.matricula.event.MatriculaConfirmadaEvento;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.DetalleMatriculaMapper;
import pe.edu.upeu.lamb.matricula.mapper.MatriculaMapper;
import pe.edu.upeu.lamb.matricula.messaging.MatriculaEventosProducer;
import pe.edu.upeu.lamb.matricula.repository.MatriculaRepository;
import pe.edu.upeu.lamb.matricula.repository.SeccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatriculaService {

    private static final String MATRICULA_CONFIRMADA = "matricula.confirmada";

    private final MatriculaRepository matriculaRepository;
    private final MatriculaMapper matriculaMapper;
    private final DetalleMatriculaMapper detalleMatriculaMapper;
    private final MatriculaEventosProducer producer;
    private final SeccionRepository seccionRepository;
    private final HistorialEstadoMatriculaService historialEstadoMatriculaService;
    private final MotivoAnulacionService motivoAnulacionService;

    public List<MatriculaResponse> listar(Long idPersona, String periodo, EstadoMatricula estado) {
        Specification<Matricula> spec = Specification.where((Specification<Matricula>) null);
        if (idPersona != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("idPersona"), idPersona));
        }
        if (periodo != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("periodo"), periodo));
        }
        if (estado != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("estado"), estado));
        }
        return matriculaRepository.findAll(spec).stream()
                .map(matriculaMapper::toResponse)
                .toList();
    }

    public MatriculaResponse obtener(Long id) {
        return matriculaMapper.toResponse(buscarOFallar(id));
    }

    /**
     * Valida cada seccion contra el catalogo local de secciones (ver Seccion/SeccionRepository):
     * matricula-ms ya no depende de planificacion-ms (fue retirado del proyecto), asi que la
     * seccion debe existir localmente y estar ABIERTA, sin fallback posible.
     */
    @Transactional
    public MatriculaResponse crear(MatriculaRequest request) {
        Matricula matricula = matriculaMapper.toEntity(request);
        matricula.setEstado(EstadoMatricula.REGISTRADA);
        for (DetalleMatricula detalle : matricula.getDetalles()) {
            validarSeccionAbierta(detalle.getIdSeccion());
        }
        Matricula guardada = matriculaRepository.save(matricula);
        historialEstadoMatriculaService.registrar(guardada.getId(), null, EstadoMatricula.REGISTRADA);
        return matriculaMapper.toResponse(guardada);
    }

    /**
     * Idempotente: si ya esta CONFIRMADA no vuelve a procesar ni a publicar el evento
     * (evita generar un cargo duplicado en finanzas si el cliente reintenta la llamada).
     * Solo se confirma desde REGISTRADA.
     */
    @Transactional
    public MatriculaResponse confirmar(Long id) {
        Matricula matricula = buscarOFallar(id);
        if (matricula.getEstado() == EstadoMatricula.CONFIRMADA) {
            log.warn("component=processor matriculaId={} status=ignored motivo=\"ya estaba confirmada\"", id);
            return matriculaMapper.toResponse(matricula);
        }
        validarEstado(matricula, EstadoMatricula.REGISTRADA, "confirmar");
        EstadoMatricula anterior = matricula.getEstado();
        matricula.setEstado(EstadoMatricula.CONFIRMADA);
        Matricula guardada = matriculaRepository.save(matricula);
        historialEstadoMatriculaService.registrar(guardada.getId(), anterior, EstadoMatricula.CONFIRMADA);

        producer.publicarTrasCommit(MatriculaConfirmadaEvento.builder()
                .tipoEvento(MATRICULA_CONFIRMADA)
                .matriculaId(guardada.getId())
                .idPersona(guardada.getIdPersona())
                .periodo(guardada.getPeriodo())
                .timestamp(Instant.now().toEpochMilli())
                .build());
        log.info("component=processor matriculaId={} idPersona={} estado={} status=processed",
                guardada.getId(), guardada.getIdPersona(), guardada.getEstado());
        return matriculaMapper.toResponse(guardada);
    }

    @Transactional
    public MatriculaResponse anular(Long id, AnularMatriculaRequest request) {
        Matricula matricula = buscarOFallar(id);
        if (matricula.getEstado() == EstadoMatricula.ANULADA) {
            log.warn("component=processor matriculaId={} status=ignored motivo=\"ya estaba anulada\"", id);
            return matriculaMapper.toResponse(matricula);
        }
        Long idMotivo = request != null ? request.getIdMotivoAnulacion() : null;
        if (idMotivo != null) {
            motivoAnulacionService.buscarOFallar(idMotivo);
            matricula.setIdMotivoAnulacion(idMotivo);
        }
        EstadoMatricula anterior = matricula.getEstado();
        matricula.setEstado(EstadoMatricula.ANULADA);
        Matricula guardada = matriculaRepository.save(matricula);
        historialEstadoMatriculaService.registrar(guardada.getId(), anterior, EstadoMatricula.ANULADA);
        return matriculaMapper.toResponse(guardada);
    }

    /**
     * Solo se puede corregir el periodo mientras la matricula sigue REGISTRADA: una vez
     * CONFIRMADA ya se publico el evento a finanzas, y ANULADA no deberia volver a editarse.
     */
    public MatriculaResponse actualizar(Long id, MatriculaUpdateRequest request) {
        Matricula matricula = buscarOFallar(id);
        validarEstado(matricula, EstadoMatricula.REGISTRADA, "editar");
        matricula.setPeriodo(request.getPeriodo());
        return matriculaMapper.toResponse(matriculaRepository.save(matricula));
    }

    public List<DetalleMatriculaResponse> listarDetalles(Long id) {
        return buscarOFallar(id).getDetalles().stream()
                .map(detalleMatriculaMapper::toResponse)
                .toList();
    }

    public DetalleMatriculaResponse obtenerDetalle(Long id, Long detalleId) {
        Matricula matricula = buscarOFallar(id);
        DetalleMatricula detalle = matricula.getDetalles().stream()
                .filter(d -> d.getId().equals(detalleId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Detalle " + detalleId + " no encontrado en la matricula " + id));
        return detalleMatriculaMapper.toResponse(detalle);
    }

    /**
     * Agregar un curso a una matricula ya registrada, sin tener que anularla y rehacerla.
     */
    @Transactional
    public DetalleMatriculaResponse agregarDetalle(Long id, DetalleMatriculaRequest request) {
        Matricula matricula = buscarOFallar(id);
        validarEstado(matricula, EstadoMatricula.REGISTRADA, "modificar");
        validarSeccionAbierta(request.getIdSeccion());

        DetalleMatricula detalle = detalleMatriculaMapper.toEntity(request);
        detalle.setMatricula(matricula);
        matricula.getDetalles().add(detalle);
        matriculaRepository.save(matricula);
        return detalleMatriculaMapper.toResponse(detalle);
    }

    /**
     * Retiro de un solo curso (distinto de anular toda la matricula). orphanRemoval en
     * Matricula.detalles borra la fila al quitarla de la lista.
     */
    @Transactional
    public void retirarDetalle(Long id, Long detalleId) {
        Matricula matricula = buscarOFallar(id);
        validarEstado(matricula, EstadoMatricula.REGISTRADA, "modificar");
        boolean eliminado = matricula.getDetalles().removeIf(d -> d.getId().equals(detalleId));
        if (!eliminado) {
            throw new ResourceNotFoundException("Detalle " + detalleId + " no encontrado en la matricula " + id);
        }
        matriculaRepository.save(matricula);
    }

    /**
     * Mover un curso de una seccion a otra sin pasar por retirar + agregar.
     */
    @Transactional
    public DetalleMatriculaResponse cambiarSeccion(Long id, Long detalleId, CambiarSeccionRequest request) {
        Matricula matricula = buscarOFallar(id);
        validarEstado(matricula, EstadoMatricula.REGISTRADA, "modificar");
        DetalleMatricula detalle = matricula.getDetalles().stream()
                .filter(d -> d.getId().equals(detalleId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Detalle " + detalleId + " no encontrado en la matricula " + id));
        validarSeccionAbierta(request.getIdSeccionNueva());
        detalle.setIdSeccion(request.getIdSeccionNueva());
        matriculaRepository.save(matricula);
        return detalleMatriculaMapper.toResponse(detalle);
    }

    public List<HistorialEstadoMatriculaResponse> historial(Long id) {
        buscarOFallar(id);
        return historialEstadoMatriculaService.listarPorMatricula(id);
    }

    public ConstanciaMatriculaResponse constancia(Long id) {
        Matricula matricula = buscarOFallar(id);
        int totalCreditos = matricula.getDetalles().stream()
                .mapToInt(DetalleMatricula::getCreditos)
                .sum();
        return ConstanciaMatriculaResponse.builder()
                .matriculaId(matricula.getId())
                .idPersona(matricula.getIdPersona())
                .periodo(matricula.getPeriodo())
                .estado(matricula.getEstado())
                .fecha(matricula.getFecha())
                .totalCreditos(totalCreditos)
                .cursos(matricula.getDetalles().stream().map(detalleMatriculaMapper::toResponse).toList())
                .fechaEmision(Instant.now())
                .build();
    }

    private void validarSeccionAbierta(Long idSeccion) {
        Seccion seccion = seccionRepository.findById(idSeccion)
                .orElseThrow(() -> new BusinessRuleException("La seccion " + idSeccion + " no existe"));
        if (seccion.getEstado() != EstadoSeccion.ABIERTA) {
            throw new BusinessRuleException("La seccion " + idSeccion
                    + " no esta ABIERTA (estado actual: " + seccion.getEstado() + ")");
        }
    }

    private void validarEstado(Matricula matricula, EstadoMatricula esperado, String accion) {
        if (matricula.getEstado() != esperado) {
            throw new BusinessRuleException("No se puede " + accion + " la matricula " + matricula.getId()
                    + ": esta en estado " + matricula.getEstado() + ", se esperaba " + esperado);
        }
    }

    private Matricula buscarOFallar(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Matricula no encontrada: " + id));
    }
}

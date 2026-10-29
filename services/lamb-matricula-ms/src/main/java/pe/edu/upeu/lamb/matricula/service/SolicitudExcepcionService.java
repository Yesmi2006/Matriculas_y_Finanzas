package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionRequest;
import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionResponse;
import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionUpdateRequest;
import pe.edu.upeu.lamb.matricula.entity.EstadoSolicitudExcepcion;
import pe.edu.upeu.lamb.matricula.entity.SolicitudExcepcion;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.SolicitudExcepcionMapper;
import pe.edu.upeu.lamb.matricula.repository.SolicitudExcepcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudExcepcionService {

    private final SolicitudExcepcionRepository solicitudExcepcionRepository;
    private final SolicitudExcepcionMapper solicitudExcepcionMapper;

    public List<SolicitudExcepcionResponse> listar(Long idPersona, EstadoSolicitudExcepcion estado) {
        List<SolicitudExcepcion> solicitudes;
        if (idPersona != null) {
            solicitudes = solicitudExcepcionRepository.findByIdPersona(idPersona);
        } else if (estado != null) {
            solicitudes = solicitudExcepcionRepository.findByEstado(estado);
        } else {
            solicitudes = solicitudExcepcionRepository.findAll();
        }
        if (idPersona != null && estado != null) {
            solicitudes = solicitudes.stream().filter(s -> s.getEstado() == estado).toList();
        }
        return solicitudes.stream().map(solicitudExcepcionMapper::toResponse).toList();
    }

    public SolicitudExcepcionResponse obtener(Long id) {
        return solicitudExcepcionMapper.toResponse(buscarOFallar(id));
    }

    public SolicitudExcepcionResponse crear(SolicitudExcepcionRequest request) {
        return solicitudExcepcionMapper.toResponse(
                solicitudExcepcionRepository.save(solicitudExcepcionMapper.toEntity(request)));
    }

    /**
     * Solo se puede editar el motivo mientras la solicitud sigue PENDIENTE: una vez aprobada
     * o rechazada ya no deberia modificarse.
     */
    public SolicitudExcepcionResponse actualizar(Long id, SolicitudExcepcionUpdateRequest request) {
        SolicitudExcepcion solicitud = buscarOFallar(id);
        if (solicitud.getEstado() != EstadoSolicitudExcepcion.PENDIENTE) {
            throw new BusinessRuleException("La solicitud " + id + " ya fue resuelta (estado actual: "
                    + solicitud.getEstado() + ")");
        }
        solicitud.setMotivo(request.getMotivo());
        return solicitudExcepcionMapper.toResponse(solicitudExcepcionRepository.save(solicitud));
    }

    public SolicitudExcepcionResponse aprobar(Long id) {
        return cambiarEstado(id, EstadoSolicitudExcepcion.APROBADA);
    }

    public SolicitudExcepcionResponse rechazar(Long id) {
        return cambiarEstado(id, EstadoSolicitudExcepcion.RECHAZADA);
    }

    public void eliminar(Long id) {
        solicitudExcepcionRepository.delete(buscarOFallar(id));
    }

    private SolicitudExcepcionResponse cambiarEstado(Long id, EstadoSolicitudExcepcion nuevoEstado) {
        SolicitudExcepcion solicitud = buscarOFallar(id);
        if (solicitud.getEstado() != EstadoSolicitudExcepcion.PENDIENTE) {
            throw new BusinessRuleException("La solicitud " + id + " ya fue resuelta (estado actual: "
                    + solicitud.getEstado() + ")");
        }
        solicitud.setEstado(nuevoEstado);
        return solicitudExcepcionMapper.toResponse(solicitudExcepcionRepository.save(solicitud));
    }

    private SolicitudExcepcion buscarOFallar(Long id) {
        return solicitudExcepcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud de excepcion no encontrada: " + id));
    }
}

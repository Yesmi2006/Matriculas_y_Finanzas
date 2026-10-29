package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionRequest;
import pe.edu.upeu.lamb.matricula.dto.SolicitudExcepcionResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoSolicitudExcepcion;
import pe.edu.upeu.lamb.matricula.entity.SolicitudExcepcion;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SolicitudExcepcionMapper {

    public SolicitudExcepcion toEntity(SolicitudExcepcionRequest request) {
        return SolicitudExcepcion.builder()
                .idPersona(request.getIdPersona())
                .idSeccion(request.getIdSeccion())
                .idMatricula(request.getIdMatricula())
                .motivo(request.getMotivo())
                .estado(EstadoSolicitudExcepcion.PENDIENTE)
                .fechaSolicitud(Instant.now())
                .build();
    }

    public SolicitudExcepcionResponse toResponse(SolicitudExcepcion solicitud) {
        return SolicitudExcepcionResponse.builder()
                .id(solicitud.getId())
                .idPersona(solicitud.getIdPersona())
                .idSeccion(solicitud.getIdSeccion())
                .idMatricula(solicitud.getIdMatricula())
                .motivo(solicitud.getMotivo())
                .estado(solicitud.getEstado())
                .fechaSolicitud(solicitud.getFechaSolicitud())
                .build();
    }
}

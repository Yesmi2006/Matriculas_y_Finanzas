package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.HistorialEstadoMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.HistorialEstadoMatricula;
import org.springframework.stereotype.Component;

@Component
public class HistorialEstadoMatriculaMapper {

    public HistorialEstadoMatriculaResponse toResponse(HistorialEstadoMatricula historial) {
        return HistorialEstadoMatriculaResponse.builder()
                .id(historial.getId())
                .idMatricula(historial.getIdMatricula())
                .estadoAnterior(historial.getEstadoAnterior())
                .estadoNuevo(historial.getEstadoNuevo())
                .fecha(historial.getFecha())
                .build();
    }
}

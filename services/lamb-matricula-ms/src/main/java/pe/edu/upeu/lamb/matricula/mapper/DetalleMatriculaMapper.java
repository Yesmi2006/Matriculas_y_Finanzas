package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.DetalleMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.DetalleMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.DetalleMatricula;
import org.springframework.stereotype.Component;

@Component
public class DetalleMatriculaMapper {

    public DetalleMatricula toEntity(DetalleMatriculaRequest request) {
        return DetalleMatricula.builder()
                .idSeccion(request.getIdSeccion())
                .creditos(request.getCreditos())
                .build();
    }

    public DetalleMatriculaResponse toResponse(DetalleMatricula detalle) {
        return DetalleMatriculaResponse.builder()
                .id(detalle.getId())
                .idSeccion(detalle.getIdSeccion())
                .creditos(detalle.getCreditos())
                .build();
    }
}

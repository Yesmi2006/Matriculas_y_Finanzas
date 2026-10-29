package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.SeccionRequest;
import pe.edu.upeu.lamb.matricula.dto.SeccionResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoSeccion;
import pe.edu.upeu.lamb.matricula.entity.Seccion;
import org.springframework.stereotype.Component;

@Component
public class SeccionMapper {

    public Seccion toEntity(SeccionRequest request) {
        return Seccion.builder()
                .idCurso(request.getIdCurso())
                .idPeriodoAcademico(request.getIdPeriodoAcademico())
                .cupoMaximo(request.getCupoMaximo())
                .estado(EstadoSeccion.ABIERTA)
                .build();
    }

    public SeccionResponse toResponse(Seccion seccion, long cupoOcupado) {
        int ocupado = (int) cupoOcupado;
        return SeccionResponse.builder()
                .id(seccion.getId())
                .idCurso(seccion.getIdCurso())
                .idPeriodoAcademico(seccion.getIdPeriodoAcademico())
                .cupoMaximo(seccion.getCupoMaximo())
                .cupoOcupado(ocupado)
                .cupoDisponible(Math.max(0, seccion.getCupoMaximo() - ocupado))
                .estado(seccion.getEstado())
                .build();
    }
}

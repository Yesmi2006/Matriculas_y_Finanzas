package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.CronogramaMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.CronogramaMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.CronogramaMatricula;
import org.springframework.stereotype.Component;

@Component
public class CronogramaMatriculaMapper {

    public CronogramaMatricula toEntity(CronogramaMatriculaRequest request) {
        return CronogramaMatricula.builder()
                .idPeriodoAcademico(request.getIdPeriodoAcademico())
                .idTipoMatricula(request.getIdTipoMatricula())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .build();
    }

    public void actualizar(CronogramaMatricula cronograma, CronogramaMatriculaRequest request) {
        cronograma.setIdPeriodoAcademico(request.getIdPeriodoAcademico());
        cronograma.setIdTipoMatricula(request.getIdTipoMatricula());
        cronograma.setFechaInicio(request.getFechaInicio());
        cronograma.setFechaFin(request.getFechaFin());
    }

    public CronogramaMatriculaResponse toResponse(CronogramaMatricula cronograma) {
        return CronogramaMatriculaResponse.builder()
                .id(cronograma.getId())
                .idPeriodoAcademico(cronograma.getIdPeriodoAcademico())
                .idTipoMatricula(cronograma.getIdTipoMatricula())
                .fechaInicio(cronograma.getFechaInicio())
                .fechaFin(cronograma.getFechaFin())
                .build();
    }
}

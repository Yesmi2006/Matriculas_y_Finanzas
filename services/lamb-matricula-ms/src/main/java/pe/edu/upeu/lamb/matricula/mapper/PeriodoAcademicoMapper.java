package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.PeriodoAcademicoRequest;
import pe.edu.upeu.lamb.matricula.dto.PeriodoAcademicoResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoPeriodo;
import pe.edu.upeu.lamb.matricula.entity.PeriodoAcademico;
import org.springframework.stereotype.Component;

@Component
public class PeriodoAcademicoMapper {

    public PeriodoAcademico toEntity(PeriodoAcademicoRequest request) {
        return PeriodoAcademico.builder()
                .codigo(request.getCodigo())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .fechaLimiteMatricula(request.getFechaLimiteMatricula())
                .estado(EstadoPeriodo.ABIERTO)
                .build();
    }

    public void actualizar(PeriodoAcademico periodo, PeriodoAcademicoRequest request) {
        periodo.setCodigo(request.getCodigo());
        periodo.setFechaInicio(request.getFechaInicio());
        periodo.setFechaFin(request.getFechaFin());
        periodo.setFechaLimiteMatricula(request.getFechaLimiteMatricula());
    }

    public PeriodoAcademicoResponse toResponse(PeriodoAcademico periodo) {
        return PeriodoAcademicoResponse.builder()
                .id(periodo.getId())
                .codigo(periodo.getCodigo())
                .fechaInicio(periodo.getFechaInicio())
                .fechaFin(periodo.getFechaFin())
                .fechaLimiteMatricula(periodo.getFechaLimiteMatricula())
                .estado(periodo.getEstado())
                .build();
    }
}

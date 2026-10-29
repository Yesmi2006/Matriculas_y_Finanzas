package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CronogramaMatriculaRequest {

    @NotNull
    private Long idPeriodoAcademico;

    @NotNull
    private Long idTipoMatricula;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;
}

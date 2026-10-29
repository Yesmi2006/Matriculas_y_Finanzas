package pe.edu.upeu.lamb.matricula.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CronogramaMatriculaResponse {
    private Long id;
    private Long idPeriodoAcademico;
    private Long idTipoMatricula;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}

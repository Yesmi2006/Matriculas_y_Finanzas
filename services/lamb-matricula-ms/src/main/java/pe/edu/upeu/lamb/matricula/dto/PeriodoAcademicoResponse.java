package pe.edu.upeu.lamb.matricula.dto;

import pe.edu.upeu.lamb.matricula.entity.EstadoPeriodo;
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
public class PeriodoAcademicoResponse {
    private Long id;
    private String codigo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaLimiteMatricula;
    private EstadoPeriodo estado;
}

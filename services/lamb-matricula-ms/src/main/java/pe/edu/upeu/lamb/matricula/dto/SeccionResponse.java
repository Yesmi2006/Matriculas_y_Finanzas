package pe.edu.upeu.lamb.matricula.dto;

import pe.edu.upeu.lamb.matricula.entity.EstadoSeccion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeccionResponse {
    private Long id;
    private Long idCurso;
    private Long idPeriodoAcademico;
    private Integer cupoMaximo;
    private Integer cupoOcupado;
    private Integer cupoDisponible;
    private EstadoSeccion estado;
}

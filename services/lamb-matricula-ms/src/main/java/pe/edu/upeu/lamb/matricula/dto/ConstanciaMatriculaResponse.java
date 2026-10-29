package pe.edu.upeu.lamb.matricula.dto;

import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConstanciaMatriculaResponse {
    private Long matriculaId;
    private Long idPersona;
    private String periodo;
    private EstadoMatricula estado;
    private Instant fecha;
    private Integer totalCreditos;
    private List<DetalleMatriculaResponse> cursos;
    private Instant fechaEmision;
}

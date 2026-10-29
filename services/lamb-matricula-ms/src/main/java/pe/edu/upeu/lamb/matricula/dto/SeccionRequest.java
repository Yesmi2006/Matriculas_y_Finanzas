package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeccionRequest {

    @NotNull
    private Long idCurso;

    @NotNull
    private Long idPeriodoAcademico;

    @NotNull
    @Min(1)
    private Integer cupoMaximo;
}

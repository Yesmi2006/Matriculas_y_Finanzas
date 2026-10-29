package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PeriodoAcademicoRequest {

    @NotBlank
    @Size(max = 20)
    private String codigo;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotNull
    private LocalDate fechaLimiteMatricula;
}

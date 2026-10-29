package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudExcepcionRequest {

    @NotNull
    private Long idPersona;

    private Long idSeccion;

    private Long idMatricula;

    @NotBlank
    @Size(max = 250)
    private String motivo;
}

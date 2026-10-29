package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambiarSeccionRequest {

    @NotNull
    private Long idSeccionNueva;
}

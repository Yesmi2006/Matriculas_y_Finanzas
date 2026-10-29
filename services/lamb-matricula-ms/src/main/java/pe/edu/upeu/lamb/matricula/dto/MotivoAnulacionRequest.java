package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotivoAnulacionRequest {

    @NotBlank
    @Size(max = 30)
    private String codigo;

    @NotBlank
    @Size(max = 150)
    private String descripcion;

    private Boolean vigente;
}

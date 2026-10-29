package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatriculaRequest {

    @NotNull
    private Long idPersona;

    @NotBlank
    @Size(max = 20)
    private String periodo;

    @NotEmpty
    @Valid
    private List<DetalleMatriculaRequest> detalles;
}

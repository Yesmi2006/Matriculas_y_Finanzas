package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatriculaUpdateRequest {

    @NotBlank
    @Size(max = 20)
    private String periodo;
}

package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TipoMatriculaRequest {

    @NotBlank
    @Size(max = 30)
    private String codigo;

    @NotBlank
    @Size(max = 150)
    private String descripcion;

    @NotNull
    @Min(0)
    private Integer diasPlazoExtra;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal recargo;

    private Boolean vigente;
}

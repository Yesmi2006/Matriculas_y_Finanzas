package pe.edu.upeu.lamb.finanzas.dto;

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
public class ConfiguracionMoraRequest {

    @NotBlank
    @Size(max = 30)
    private String codigo;

    @NotNull
    @Min(0)
    private Integer diasGracia;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal porcentajeMoraDiario;

    private Boolean vigente;
}

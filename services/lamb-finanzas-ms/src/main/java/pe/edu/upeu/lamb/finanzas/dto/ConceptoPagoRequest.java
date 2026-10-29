package pe.edu.upeu.lamb.finanzas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ConceptoPagoRequest {

    @NotBlank
    @Size(max = 50)
    private String codigo;

    @NotBlank
    @Size(max = 150)
    private String descripcion;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal monto;

    private Boolean vigente;
}

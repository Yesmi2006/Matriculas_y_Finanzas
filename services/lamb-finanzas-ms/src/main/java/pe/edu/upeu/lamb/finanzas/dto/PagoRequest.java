package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.MetodoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagoRequest {

    @NotNull
    private Long idPersona;

    @NotBlank
    @Size(max = 150)
    private String concepto;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal monto;

    @NotNull
    private MetodoPago metodo;
}

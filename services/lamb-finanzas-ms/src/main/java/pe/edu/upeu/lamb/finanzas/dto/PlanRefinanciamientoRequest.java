package pe.edu.upeu.lamb.finanzas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PlanRefinanciamientoRequest {

    @NotNull
    private Long idPersona;

    @NotNull
    private Long idPagoOriginal;

    @NotNull
    @Min(1)
    private Integer numeroCuotas;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal montoTotal;
}

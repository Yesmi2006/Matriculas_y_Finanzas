package pe.edu.upeu.lamb.finanzas.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class CuotaPagoRequest {

    @NotNull
    private Integer numeroCuota;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal montoCuota;

    @NotNull
    private Instant fechaVencimiento;
}

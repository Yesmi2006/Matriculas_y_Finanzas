package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.EstadoCuota;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuotaPagoResponse {
    private Long id;
    private Long idPago;
    private Integer numeroCuota;
    private BigDecimal montoCuota;
    private Instant fechaVencimiento;
    private EstadoCuota estado;
    private Instant fechaPago;
}

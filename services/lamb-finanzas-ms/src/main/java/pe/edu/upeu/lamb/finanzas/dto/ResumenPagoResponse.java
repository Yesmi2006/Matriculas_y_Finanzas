package pe.edu.upeu.lamb.finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenPagoResponse {
    private Long idPersona;
    private BigDecimal totalPendiente;
    private BigDecimal totalAprobado;
    private BigDecimal totalReembolsado;
}

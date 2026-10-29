package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.EstadoPlanRefinanciamiento;
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
public class PlanRefinanciamientoResponse {
    private Long id;
    private Long idPersona;
    private Long idPagoOriginal;
    private Integer numeroCuotas;
    private BigDecimal montoTotal;
    private Instant fechaCreacion;
    private EstadoPlanRefinanciamiento estado;
}

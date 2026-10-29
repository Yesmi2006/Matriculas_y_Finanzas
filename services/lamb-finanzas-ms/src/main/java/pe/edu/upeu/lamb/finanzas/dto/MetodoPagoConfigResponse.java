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
public class MetodoPagoConfigResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private BigDecimal comision;
    private boolean vigente;
}

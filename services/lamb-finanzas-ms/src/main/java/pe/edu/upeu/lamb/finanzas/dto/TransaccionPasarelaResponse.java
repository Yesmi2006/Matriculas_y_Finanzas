package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.EstadoTransaccionPasarela;
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
public class TransaccionPasarelaResponse {
    private Long id;
    private Long idPago;
    private String referencia;
    private BigDecimal monto;
    private EstadoTransaccionPasarela estado;
    private Instant fecha;
}

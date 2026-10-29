package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.EstadoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialEstadoPagoResponse {
    private Long id;
    private Long idPago;
    private EstadoPago estadoAnterior;
    private EstadoPago estadoNuevo;
    private Instant fecha;
}

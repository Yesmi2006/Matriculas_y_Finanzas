package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.TipoMotivoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotivoPagoResponse {
    private Long id;
    private TipoMotivoPago tipo;
    private String codigo;
    private String descripcion;
    private boolean vigente;
}

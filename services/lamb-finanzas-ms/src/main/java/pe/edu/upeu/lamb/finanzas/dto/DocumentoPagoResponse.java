package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.EstadoDocumento;
import pe.edu.upeu.lamb.finanzas.entity.TipoDocumento;
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
public class DocumentoPagoResponse {
    private Long id;
    private Long idPago;
    private TipoDocumento tipo;
    private String numero;
    private EstadoDocumento estado;
    private Instant fechaEmision;
}

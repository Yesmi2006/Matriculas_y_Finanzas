package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.TipoDocumento;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentoPagoRequest {

    @NotNull
    private Long idPago;

    @NotNull
    private TipoDocumento tipo;
}

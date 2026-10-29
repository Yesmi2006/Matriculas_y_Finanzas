package pe.edu.upeu.lamb.finanzas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RechazarPagoRequest {

    // Opcional: referencia a un MotivoPago (tipo=RECHAZO) vigente. Sin cuerpo tambien es valido.
    private Long idMotivoPago;
}

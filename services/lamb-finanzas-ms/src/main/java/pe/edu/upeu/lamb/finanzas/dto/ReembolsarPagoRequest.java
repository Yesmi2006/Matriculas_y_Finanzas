package pe.edu.upeu.lamb.finanzas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReembolsarPagoRequest {

    // Opcional: referencia a un MotivoPago (tipo=REEMBOLSO) vigente. Sin cuerpo tambien es valido.
    private Long idMotivoPago;
}

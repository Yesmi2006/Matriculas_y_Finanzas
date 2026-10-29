package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.TipoMotivoPago;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotivoPagoRequest {

    @NotNull
    private TipoMotivoPago tipo;

    @NotBlank
    @Size(max = 30)
    private String codigo;

    @NotBlank
    @Size(max = 150)
    private String descripcion;

    private Boolean vigente;
}

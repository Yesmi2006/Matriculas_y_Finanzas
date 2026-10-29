package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.MetodoPago;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagoUpdateRequest {

    @NotNull
    private MetodoPago metodo;
}

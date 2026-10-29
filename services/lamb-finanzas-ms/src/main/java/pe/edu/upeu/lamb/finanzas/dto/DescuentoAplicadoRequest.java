package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.TipoDescuento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DescuentoAplicadoRequest {

    @NotNull
    private Long idPersona;

    private Long idPago;

    @NotNull
    private TipoDescuento tipo;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal valor;

    @NotBlank
    @Size(max = 150)
    private String descripcion;
}

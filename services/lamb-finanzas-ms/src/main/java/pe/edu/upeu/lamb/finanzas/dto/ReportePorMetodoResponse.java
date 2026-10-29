package pe.edu.upeu.lamb.finanzas.dto;

import pe.edu.upeu.lamb.finanzas.entity.MetodoPago;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportePorMetodoResponse {
    private MetodoPago metodo;
    private long cantidad;
    private BigDecimal montoTotal;
}

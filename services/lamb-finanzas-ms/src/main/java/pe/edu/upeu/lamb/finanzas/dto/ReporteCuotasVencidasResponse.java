package pe.edu.upeu.lamb.finanzas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReporteCuotasVencidasResponse {
    private long cantidad;
    private BigDecimal montoTotal;
    private List<CuotaPagoResponse> cuotas;
}

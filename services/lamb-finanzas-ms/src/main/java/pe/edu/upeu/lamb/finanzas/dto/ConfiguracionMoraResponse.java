package pe.edu.upeu.lamb.finanzas.dto;

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
public class ConfiguracionMoraResponse {
    private Long id;
    private String codigo;
    private Integer diasGracia;
    private BigDecimal porcentajeMoraDiario;
    private boolean vigente;
}

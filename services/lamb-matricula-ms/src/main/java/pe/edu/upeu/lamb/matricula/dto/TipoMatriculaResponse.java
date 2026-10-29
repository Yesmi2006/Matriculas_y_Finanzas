package pe.edu.upeu.lamb.matricula.dto;

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
public class TipoMatriculaResponse {
    private Long id;
    private String codigo;
    private String descripcion;
    private Integer diasPlazoExtra;
    private BigDecimal recargo;
    private boolean vigente;
}

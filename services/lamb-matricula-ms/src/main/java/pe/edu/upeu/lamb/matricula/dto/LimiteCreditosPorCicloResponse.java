package pe.edu.upeu.lamb.matricula.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LimiteCreditosPorCicloResponse {
    private Long id;
    private Integer ciclo;
    private Integer creditosMaximos;
}

package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LimiteCreditosPorCicloRequest {

    @NotNull
    @Min(1)
    @Max(15)
    private Integer ciclo;

    @NotNull
    @Min(1)
    private Integer creditosMaximos;
}

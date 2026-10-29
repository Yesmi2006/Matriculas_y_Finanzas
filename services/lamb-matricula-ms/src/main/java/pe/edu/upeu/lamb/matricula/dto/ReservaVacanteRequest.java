package pe.edu.upeu.lamb.matricula.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaVacanteRequest {

    @NotNull
    private Long idPersona;

    @NotNull
    private Long idSeccion;

    // Minutos que la reserva se mantiene activa antes de expirar.
    @NotNull
    @Min(1)
    private Integer minutosVigencia;
}

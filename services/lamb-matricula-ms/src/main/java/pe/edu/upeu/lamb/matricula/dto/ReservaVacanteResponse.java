package pe.edu.upeu.lamb.matricula.dto;

import pe.edu.upeu.lamb.matricula.entity.EstadoReserva;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaVacanteResponse {
    private Long id;
    private Long idPersona;
    private Long idSeccion;
    private Instant fechaReserva;
    private Instant fechaExpiracion;
    private EstadoReserva estado;
}

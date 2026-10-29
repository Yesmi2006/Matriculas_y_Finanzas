package pe.edu.upeu.lamb.matricula.dto;

import pe.edu.upeu.lamb.matricula.entity.EstadoSolicitudExcepcion;
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
public class SolicitudExcepcionResponse {
    private Long id;
    private Long idPersona;
    private Long idSeccion;
    private Long idMatricula;
    private String motivo;
    private EstadoSolicitudExcepcion estado;
    private Instant fechaSolicitud;
}

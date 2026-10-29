package pe.edu.upeu.lamb.matricula.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento matricula.confirmada del topic matricula-eventos (key = idPersona). Lo consume finanzas
 * para generar el cargo de matricula del periodo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaConfirmadaEvento {

    private String tipoEvento;
    private Long matriculaId;
    private Long idPersona;
    private String periodo;
    private Long timestamp;
}

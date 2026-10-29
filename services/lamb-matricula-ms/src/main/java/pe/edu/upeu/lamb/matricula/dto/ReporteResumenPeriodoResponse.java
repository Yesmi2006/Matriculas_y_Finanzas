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
public class ReporteResumenPeriodoResponse {
    private String periodo;
    private long registradas;
    private long confirmadas;
    private long anuladas;
    private long total;
}

package pe.edu.upeu.lamb.matricula.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnularMatriculaRequest {

    // Opcional: referencia a un MotivoAnulacion vigente. Sin cuerpo tambien es valido.
    private Long idMotivoAnulacion;
}

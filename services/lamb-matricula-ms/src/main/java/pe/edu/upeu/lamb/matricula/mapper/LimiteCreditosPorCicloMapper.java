package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.LimiteCreditosPorCicloRequest;
import pe.edu.upeu.lamb.matricula.dto.LimiteCreditosPorCicloResponse;
import pe.edu.upeu.lamb.matricula.entity.LimiteCreditosPorCiclo;
import org.springframework.stereotype.Component;

@Component
public class LimiteCreditosPorCicloMapper {

    public LimiteCreditosPorCiclo toEntity(LimiteCreditosPorCicloRequest request) {
        return LimiteCreditosPorCiclo.builder()
                .ciclo(request.getCiclo())
                .creditosMaximos(request.getCreditosMaximos())
                .build();
    }

    public void actualizar(LimiteCreditosPorCiclo limite, LimiteCreditosPorCicloRequest request) {
        limite.setCiclo(request.getCiclo());
        limite.setCreditosMaximos(request.getCreditosMaximos());
    }

    public LimiteCreditosPorCicloResponse toResponse(LimiteCreditosPorCiclo limite) {
        return LimiteCreditosPorCicloResponse.builder()
                .id(limite.getId())
                .ciclo(limite.getCiclo())
                .creditosMaximos(limite.getCreditosMaximos())
                .build();
    }
}

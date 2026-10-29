package pe.edu.upeu.lamb.matricula.mapper;

import pe.edu.upeu.lamb.matricula.dto.ReservaVacanteResponse;
import pe.edu.upeu.lamb.matricula.entity.ReservaVacante;
import org.springframework.stereotype.Component;

@Component
public class ReservaVacanteMapper {

    public ReservaVacanteResponse toResponse(ReservaVacante reserva) {
        return ReservaVacanteResponse.builder()
                .id(reserva.getId())
                .idPersona(reserva.getIdPersona())
                .idSeccion(reserva.getIdSeccion())
                .fechaReserva(reserva.getFechaReserva())
                .fechaExpiracion(reserva.getFechaExpiracion())
                .estado(reserva.getEstado())
                .build();
    }
}

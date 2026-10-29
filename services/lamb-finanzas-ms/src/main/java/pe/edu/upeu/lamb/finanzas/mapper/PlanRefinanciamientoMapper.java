package pe.edu.upeu.lamb.finanzas.mapper;

import pe.edu.upeu.lamb.finanzas.dto.PlanRefinanciamientoRequest;
import pe.edu.upeu.lamb.finanzas.dto.PlanRefinanciamientoResponse;
import pe.edu.upeu.lamb.finanzas.entity.EstadoPlanRefinanciamiento;
import pe.edu.upeu.lamb.finanzas.entity.PlanRefinanciamiento;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class PlanRefinanciamientoMapper {

    public PlanRefinanciamiento toEntity(PlanRefinanciamientoRequest request) {
        return PlanRefinanciamiento.builder()
                .idPersona(request.getIdPersona())
                .idPagoOriginal(request.getIdPagoOriginal())
                .numeroCuotas(request.getNumeroCuotas())
                .montoTotal(request.getMontoTotal())
                .fechaCreacion(Instant.now())
                .estado(EstadoPlanRefinanciamiento.ACTIVO)
                .build();
    }

    public PlanRefinanciamientoResponse toResponse(PlanRefinanciamiento plan) {
        return PlanRefinanciamientoResponse.builder()
                .id(plan.getId())
                .idPersona(plan.getIdPersona())
                .idPagoOriginal(plan.getIdPagoOriginal())
                .numeroCuotas(plan.getNumeroCuotas())
                .montoTotal(plan.getMontoTotal())
                .fechaCreacion(plan.getFechaCreacion())
                .estado(plan.getEstado())
                .build();
    }
}

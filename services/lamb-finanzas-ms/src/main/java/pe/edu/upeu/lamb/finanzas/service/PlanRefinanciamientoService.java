package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.PlanRefinanciamientoRequest;
import pe.edu.upeu.lamb.finanzas.dto.PlanRefinanciamientoResponse;
import pe.edu.upeu.lamb.finanzas.entity.EstadoPlanRefinanciamiento;
import pe.edu.upeu.lamb.finanzas.entity.PlanRefinanciamiento;
import pe.edu.upeu.lamb.finanzas.exception.BusinessRuleException;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.PlanRefinanciamientoMapper;
import pe.edu.upeu.lamb.finanzas.repository.PlanRefinanciamientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanRefinanciamientoService {

    private final PlanRefinanciamientoRepository planRefinanciamientoRepository;
    private final PlanRefinanciamientoMapper planRefinanciamientoMapper;

    public List<PlanRefinanciamientoResponse> listar(Long idPersona) {
        List<PlanRefinanciamiento> planes = idPersona != null
                ? planRefinanciamientoRepository.findByIdPersona(idPersona)
                : planRefinanciamientoRepository.findAll();
        return planes.stream().map(planRefinanciamientoMapper::toResponse).toList();
    }

    public PlanRefinanciamientoResponse obtener(Long id) {
        return planRefinanciamientoMapper.toResponse(buscarOFallar(id));
    }

    public PlanRefinanciamientoResponse crear(PlanRefinanciamientoRequest request) {
        return planRefinanciamientoMapper.toResponse(
                planRefinanciamientoRepository.save(planRefinanciamientoMapper.toEntity(request)));
    }

    public PlanRefinanciamientoResponse completar(Long id) {
        PlanRefinanciamiento plan = buscarOFallar(id);
        validarActivo(plan);
        plan.setEstado(EstadoPlanRefinanciamiento.COMPLETADO);
        return planRefinanciamientoMapper.toResponse(planRefinanciamientoRepository.save(plan));
    }

    public PlanRefinanciamientoResponse cancelar(Long id) {
        PlanRefinanciamiento plan = buscarOFallar(id);
        validarActivo(plan);
        plan.setEstado(EstadoPlanRefinanciamiento.CANCELADO);
        return planRefinanciamientoMapper.toResponse(planRefinanciamientoRepository.save(plan));
    }

    private void validarActivo(PlanRefinanciamiento plan) {
        if (plan.getEstado() != EstadoPlanRefinanciamiento.ACTIVO) {
            throw new BusinessRuleException("El plan de refinanciamiento " + plan.getId()
                    + " no esta ACTIVO (estado actual=" + plan.getEstado() + ")");
        }
    }

    private PlanRefinanciamiento buscarOFallar(Long id) {
        return planRefinanciamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan de refinanciamiento no encontrado: " + id));
    }
}

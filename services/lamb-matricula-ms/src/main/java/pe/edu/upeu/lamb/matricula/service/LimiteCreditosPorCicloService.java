package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.LimiteCreditosPorCicloRequest;
import pe.edu.upeu.lamb.matricula.dto.LimiteCreditosPorCicloResponse;
import pe.edu.upeu.lamb.matricula.entity.LimiteCreditosPorCiclo;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.LimiteCreditosPorCicloMapper;
import pe.edu.upeu.lamb.matricula.repository.LimiteCreditosPorCicloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LimiteCreditosPorCicloService {

    private final LimiteCreditosPorCicloRepository limiteCreditosPorCicloRepository;
    private final LimiteCreditosPorCicloMapper limiteCreditosPorCicloMapper;

    public List<LimiteCreditosPorCicloResponse> listar() {
        return limiteCreditosPorCicloRepository.findAll().stream()
                .map(limiteCreditosPorCicloMapper::toResponse)
                .toList();
    }

    public LimiteCreditosPorCicloResponse obtener(Long id) {
        return limiteCreditosPorCicloMapper.toResponse(buscarOFallar(id));
    }

    public LimiteCreditosPorCicloResponse crear(LimiteCreditosPorCicloRequest request) {
        if (limiteCreditosPorCicloRepository.existsByCiclo(request.getCiclo())) {
            throw new BusinessRuleException("Ya existe un limite de creditos para el ciclo " + request.getCiclo());
        }
        return limiteCreditosPorCicloMapper.toResponse(
                limiteCreditosPorCicloRepository.save(limiteCreditosPorCicloMapper.toEntity(request)));
    }

    public LimiteCreditosPorCicloResponse actualizar(Long id, LimiteCreditosPorCicloRequest request) {
        LimiteCreditosPorCiclo limite = buscarOFallar(id);
        limiteCreditosPorCicloRepository.findByCiclo(request.getCiclo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe un limite de creditos para el ciclo " + request.getCiclo());
                });
        limiteCreditosPorCicloMapper.actualizar(limite, request);
        return limiteCreditosPorCicloMapper.toResponse(limiteCreditosPorCicloRepository.save(limite));
    }

    public void eliminar(Long id) {
        limiteCreditosPorCicloRepository.delete(buscarOFallar(id));
    }

    private LimiteCreditosPorCiclo buscarOFallar(Long id) {
        return limiteCreditosPorCicloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Limite de creditos no encontrado: " + id));
    }
}

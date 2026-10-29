package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.MotivoAnulacionRequest;
import pe.edu.upeu.lamb.matricula.dto.MotivoAnulacionResponse;
import pe.edu.upeu.lamb.matricula.entity.MotivoAnulacion;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.MotivoAnulacionMapper;
import pe.edu.upeu.lamb.matricula.repository.MotivoAnulacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotivoAnulacionService {

    private final MotivoAnulacionRepository motivoAnulacionRepository;
    private final MotivoAnulacionMapper motivoAnulacionMapper;

    public List<MotivoAnulacionResponse> listar(Boolean vigente) {
        return motivoAnulacionRepository.findAll().stream()
                .filter(m -> vigente == null || m.isVigente() == vigente)
                .map(motivoAnulacionMapper::toResponse)
                .toList();
    }

    public MotivoAnulacionResponse obtener(Long id) {
        return motivoAnulacionMapper.toResponse(buscarOFallar(id));
    }

    public MotivoAnulacionResponse crear(MotivoAnulacionRequest request) {
        if (motivoAnulacionRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessRuleException("Ya existe un motivo de anulacion con el codigo " + request.getCodigo());
        }
        return motivoAnulacionMapper.toResponse(motivoAnulacionRepository.save(motivoAnulacionMapper.toEntity(request)));
    }

    public MotivoAnulacionResponse actualizar(Long id, MotivoAnulacionRequest request) {
        MotivoAnulacion motivo = buscarOFallar(id);
        motivoAnulacionRepository.findByCodigo(request.getCodigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe un motivo de anulacion con el codigo " + request.getCodigo());
                });
        motivoAnulacionMapper.actualizar(motivo, request);
        return motivoAnulacionMapper.toResponse(motivoAnulacionRepository.save(motivo));
    }

    public void eliminar(Long id) {
        motivoAnulacionRepository.delete(buscarOFallar(id));
    }

    MotivoAnulacion buscarOFallar(Long id) {
        return motivoAnulacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motivo de anulacion no encontrado: " + id));
    }
}

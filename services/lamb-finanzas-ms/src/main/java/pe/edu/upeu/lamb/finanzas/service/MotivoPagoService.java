package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.MotivoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.MotivoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.MotivoPago;
import pe.edu.upeu.lamb.finanzas.entity.TipoMotivoPago;
import pe.edu.upeu.lamb.finanzas.exception.BusinessRuleException;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.MotivoPagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.MotivoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotivoPagoService {

    private final MotivoPagoRepository motivoPagoRepository;
    private final MotivoPagoMapper motivoPagoMapper;

    public List<MotivoPagoResponse> listar(TipoMotivoPago tipo, Boolean vigente) {
        List<MotivoPago> motivos = tipo != null
                ? motivoPagoRepository.findByTipo(tipo)
                : motivoPagoRepository.findAll();
        return motivos.stream()
                .filter(m -> vigente == null || m.isVigente() == vigente)
                .map(motivoPagoMapper::toResponse)
                .toList();
    }

    public MotivoPagoResponse obtener(Long id) {
        return motivoPagoMapper.toResponse(buscarOFallar(id));
    }

    public MotivoPagoResponse crear(MotivoPagoRequest request) {
        if (motivoPagoRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessRuleException("Ya existe un motivo de pago con el codigo " + request.getCodigo());
        }
        return motivoPagoMapper.toResponse(motivoPagoRepository.save(motivoPagoMapper.toEntity(request)));
    }

    public MotivoPagoResponse actualizar(Long id, MotivoPagoRequest request) {
        MotivoPago motivo = buscarOFallar(id);
        motivoPagoRepository.findByCodigo(request.getCodigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe un motivo de pago con el codigo " + request.getCodigo());
                });
        motivoPagoMapper.actualizar(motivo, request);
        return motivoPagoMapper.toResponse(motivoPagoRepository.save(motivo));
    }

    public void eliminar(Long id) {
        motivoPagoRepository.delete(buscarOFallar(id));
    }

    MotivoPago buscarOFallar(Long id) {
        return motivoPagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Motivo de pago no encontrado: " + id));
    }
}

package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.ConceptoPagoRequest;
import pe.edu.upeu.lamb.finanzas.dto.ConceptoPagoResponse;
import pe.edu.upeu.lamb.finanzas.entity.ConceptoPago;
import pe.edu.upeu.lamb.finanzas.exception.BusinessRuleException;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.ConceptoPagoMapper;
import pe.edu.upeu.lamb.finanzas.repository.ConceptoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConceptoPagoService {

    private final ConceptoPagoRepository conceptoPagoRepository;
    private final ConceptoPagoMapper conceptoPagoMapper;

    public List<ConceptoPagoResponse> listar(Boolean vigente) {
        return conceptoPagoRepository.findAll().stream()
                .filter(c -> vigente == null || c.isVigente() == vigente)
                .map(conceptoPagoMapper::toResponse)
                .toList();
    }

    public ConceptoPagoResponse obtener(Long id) {
        return conceptoPagoMapper.toResponse(buscarOFallar(id));
    }

    public ConceptoPagoResponse crear(ConceptoPagoRequest request) {
        if (conceptoPagoRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessRuleException("Ya existe un concepto con el codigo " + request.getCodigo());
        }
        ConceptoPago guardado = conceptoPagoRepository.save(conceptoPagoMapper.toEntity(request));
        return conceptoPagoMapper.toResponse(guardado);
    }

    public ConceptoPagoResponse actualizar(Long id, ConceptoPagoRequest request) {
        ConceptoPago concepto = buscarOFallar(id);
        conceptoPagoRepository.findByCodigo(request.getCodigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe un concepto con el codigo " + request.getCodigo());
                });
        conceptoPagoMapper.actualizar(concepto, request);
        return conceptoPagoMapper.toResponse(conceptoPagoRepository.save(concepto));
    }

    public void eliminar(Long id) {
        ConceptoPago concepto = buscarOFallar(id);
        conceptoPagoRepository.delete(concepto);
    }

    private ConceptoPago buscarOFallar(Long id) {
        return conceptoPagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Concepto de pago no encontrado: " + id));
    }
}

package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.MetodoPagoConfigRequest;
import pe.edu.upeu.lamb.finanzas.dto.MetodoPagoConfigResponse;
import pe.edu.upeu.lamb.finanzas.entity.MetodoPagoConfig;
import pe.edu.upeu.lamb.finanzas.exception.BusinessRuleException;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.MetodoPagoConfigMapper;
import pe.edu.upeu.lamb.finanzas.repository.MetodoPagoConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetodoPagoConfigService {

    private final MetodoPagoConfigRepository metodoPagoConfigRepository;
    private final MetodoPagoConfigMapper metodoPagoConfigMapper;

    public List<MetodoPagoConfigResponse> listar(Boolean vigente) {
        return metodoPagoConfigRepository.findAll().stream()
                .filter(m -> vigente == null || m.isVigente() == vigente)
                .map(metodoPagoConfigMapper::toResponse)
                .toList();
    }

    public MetodoPagoConfigResponse obtener(Long id) {
        return metodoPagoConfigMapper.toResponse(buscarOFallar(id));
    }

    public MetodoPagoConfigResponse crear(MetodoPagoConfigRequest request) {
        if (metodoPagoConfigRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessRuleException("Ya existe un metodo de pago con el codigo " + request.getCodigo());
        }
        return metodoPagoConfigMapper.toResponse(
                metodoPagoConfigRepository.save(metodoPagoConfigMapper.toEntity(request)));
    }

    public MetodoPagoConfigResponse actualizar(Long id, MetodoPagoConfigRequest request) {
        MetodoPagoConfig config = buscarOFallar(id);
        metodoPagoConfigRepository.findByCodigo(request.getCodigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe un metodo de pago con el codigo " + request.getCodigo());
                });
        metodoPagoConfigMapper.actualizar(config, request);
        return metodoPagoConfigMapper.toResponse(metodoPagoConfigRepository.save(config));
    }

    public void eliminar(Long id) {
        metodoPagoConfigRepository.delete(buscarOFallar(id));
    }

    private MetodoPagoConfig buscarOFallar(Long id) {
        return metodoPagoConfigRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Metodo de pago no encontrado: " + id));
    }
}

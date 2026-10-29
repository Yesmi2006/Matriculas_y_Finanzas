package pe.edu.upeu.lamb.finanzas.service;

import pe.edu.upeu.lamb.finanzas.dto.ConfiguracionMoraRequest;
import pe.edu.upeu.lamb.finanzas.dto.ConfiguracionMoraResponse;
import pe.edu.upeu.lamb.finanzas.entity.ConfiguracionMora;
import pe.edu.upeu.lamb.finanzas.exception.BusinessRuleException;
import pe.edu.upeu.lamb.finanzas.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.finanzas.mapper.ConfiguracionMoraMapper;
import pe.edu.upeu.lamb.finanzas.repository.ConfiguracionMoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfiguracionMoraService {

    private final ConfiguracionMoraRepository configuracionMoraRepository;
    private final ConfiguracionMoraMapper configuracionMoraMapper;

    public List<ConfiguracionMoraResponse> listar() {
        return configuracionMoraRepository.findAll().stream()
                .map(configuracionMoraMapper::toResponse)
                .toList();
    }

    public ConfiguracionMoraResponse obtener(Long id) {
        return configuracionMoraMapper.toResponse(buscarOFallar(id));
    }

    public ConfiguracionMoraResponse crear(ConfiguracionMoraRequest request) {
        if (configuracionMoraRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessRuleException("Ya existe una configuracion de mora con el codigo " + request.getCodigo());
        }
        return configuracionMoraMapper.toResponse(
                configuracionMoraRepository.save(configuracionMoraMapper.toEntity(request)));
    }

    public ConfiguracionMoraResponse actualizar(Long id, ConfiguracionMoraRequest request) {
        ConfiguracionMora config = buscarOFallar(id);
        configuracionMoraRepository.findByCodigo(request.getCodigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe una configuracion de mora con el codigo " + request.getCodigo());
                });
        configuracionMoraMapper.actualizar(config, request);
        return configuracionMoraMapper.toResponse(configuracionMoraRepository.save(config));
    }

    public void eliminar(Long id) {
        configuracionMoraRepository.delete(buscarOFallar(id));
    }

    private ConfiguracionMora buscarOFallar(Long id) {
        return configuracionMoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion de mora no encontrada: " + id));
    }
}

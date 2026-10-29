package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.TipoMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.TipoMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.TipoMatricula;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.TipoMatriculaMapper;
import pe.edu.upeu.lamb.matricula.repository.TipoMatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoMatriculaService {

    private final TipoMatriculaRepository tipoMatriculaRepository;
    private final TipoMatriculaMapper tipoMatriculaMapper;

    public List<TipoMatriculaResponse> listar(Boolean vigente) {
        return tipoMatriculaRepository.findAll().stream()
                .filter(t -> vigente == null || t.isVigente() == vigente)
                .map(tipoMatriculaMapper::toResponse)
                .toList();
    }

    public TipoMatriculaResponse obtener(Long id) {
        return tipoMatriculaMapper.toResponse(buscarOFallar(id));
    }

    public TipoMatriculaResponse crear(TipoMatriculaRequest request) {
        if (tipoMatriculaRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessRuleException("Ya existe un tipo de matricula con el codigo " + request.getCodigo());
        }
        return tipoMatriculaMapper.toResponse(tipoMatriculaRepository.save(tipoMatriculaMapper.toEntity(request)));
    }

    public TipoMatriculaResponse actualizar(Long id, TipoMatriculaRequest request) {
        TipoMatricula tipo = buscarOFallar(id);
        tipoMatriculaRepository.findByCodigo(request.getCodigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe un tipo de matricula con el codigo " + request.getCodigo());
                });
        tipoMatriculaMapper.actualizar(tipo, request);
        return tipoMatriculaMapper.toResponse(tipoMatriculaRepository.save(tipo));
    }

    public void eliminar(Long id) {
        tipoMatriculaRepository.delete(buscarOFallar(id));
    }

    private TipoMatricula buscarOFallar(Long id) {
        return tipoMatriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de matricula no encontrado: " + id));
    }
}

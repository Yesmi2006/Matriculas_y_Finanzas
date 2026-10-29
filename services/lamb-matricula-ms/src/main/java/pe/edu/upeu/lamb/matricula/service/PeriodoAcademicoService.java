package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.PeriodoAcademicoRequest;
import pe.edu.upeu.lamb.matricula.dto.PeriodoAcademicoResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoPeriodo;
import pe.edu.upeu.lamb.matricula.entity.PeriodoAcademico;
import pe.edu.upeu.lamb.matricula.exception.BusinessRuleException;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.PeriodoAcademicoMapper;
import pe.edu.upeu.lamb.matricula.repository.PeriodoAcademicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PeriodoAcademicoService {

    private final PeriodoAcademicoRepository periodoAcademicoRepository;
    private final PeriodoAcademicoMapper periodoAcademicoMapper;

    public List<PeriodoAcademicoResponse> listar(EstadoPeriodo estado) {
        List<PeriodoAcademico> periodos = estado != null
                ? periodoAcademicoRepository.findByEstado(estado)
                : periodoAcademicoRepository.findAll();
        return periodos.stream()
                .map(periodoAcademicoMapper::toResponse)
                .toList();
    }

    public PeriodoAcademicoResponse obtener(Long id) {
        return periodoAcademicoMapper.toResponse(buscarOFallar(id));
    }

    public PeriodoAcademicoResponse crear(PeriodoAcademicoRequest request) {
        if (periodoAcademicoRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessRuleException("Ya existe un periodo academico con el codigo " + request.getCodigo());
        }
        return periodoAcademicoMapper.toResponse(periodoAcademicoRepository.save(periodoAcademicoMapper.toEntity(request)));
    }

    public PeriodoAcademicoResponse actualizar(Long id, PeriodoAcademicoRequest request) {
        PeriodoAcademico periodo = buscarOFallar(id);
        periodoAcademicoRepository.findByCodigo(request.getCodigo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new BusinessRuleException("Ya existe un periodo academico con el codigo " + request.getCodigo());
                });
        periodoAcademicoMapper.actualizar(periodo, request);
        return periodoAcademicoMapper.toResponse(periodoAcademicoRepository.save(periodo));
    }

    public void eliminar(Long id) {
        periodoAcademicoRepository.delete(buscarOFallar(id));
    }

    public PeriodoAcademicoResponse cerrar(Long id) {
        PeriodoAcademico periodo = buscarOFallar(id);
        periodo.setEstado(EstadoPeriodo.CERRADO);
        return periodoAcademicoMapper.toResponse(periodoAcademicoRepository.save(periodo));
    }

    public PeriodoAcademicoResponse abrir(Long id) {
        PeriodoAcademico periodo = buscarOFallar(id);
        periodo.setEstado(EstadoPeriodo.ABIERTO);
        return periodoAcademicoMapper.toResponse(periodoAcademicoRepository.save(periodo));
    }

    private PeriodoAcademico buscarOFallar(Long id) {
        return periodoAcademicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Periodo academico no encontrado: " + id));
    }
}

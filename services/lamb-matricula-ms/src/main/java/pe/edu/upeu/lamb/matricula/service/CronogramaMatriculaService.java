package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.CronogramaMatriculaRequest;
import pe.edu.upeu.lamb.matricula.dto.CronogramaMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.CronogramaMatricula;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.CronogramaMatriculaMapper;
import pe.edu.upeu.lamb.matricula.repository.CronogramaMatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CronogramaMatriculaService {

    private final CronogramaMatriculaRepository cronogramaMatriculaRepository;
    private final CronogramaMatriculaMapper cronogramaMatriculaMapper;

    public List<CronogramaMatriculaResponse> listar(Long idPeriodoAcademico) {
        List<CronogramaMatricula> cronogramas = idPeriodoAcademico != null
                ? cronogramaMatriculaRepository.findByIdPeriodoAcademico(idPeriodoAcademico)
                : cronogramaMatriculaRepository.findAll();
        return cronogramas.stream().map(cronogramaMatriculaMapper::toResponse).toList();
    }

    public CronogramaMatriculaResponse obtener(Long id) {
        return cronogramaMatriculaMapper.toResponse(buscarOFallar(id));
    }

    public CronogramaMatriculaResponse crear(CronogramaMatriculaRequest request) {
        return cronogramaMatriculaMapper.toResponse(
                cronogramaMatriculaRepository.save(cronogramaMatriculaMapper.toEntity(request)));
    }

    public CronogramaMatriculaResponse actualizar(Long id, CronogramaMatriculaRequest request) {
        CronogramaMatricula cronograma = buscarOFallar(id);
        cronogramaMatriculaMapper.actualizar(cronograma, request);
        return cronogramaMatriculaMapper.toResponse(cronogramaMatriculaRepository.save(cronograma));
    }

    public void eliminar(Long id) {
        cronogramaMatriculaRepository.delete(buscarOFallar(id));
    }

    private CronogramaMatricula buscarOFallar(Long id) {
        return cronogramaMatriculaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cronograma de matricula no encontrado: " + id));
    }
}

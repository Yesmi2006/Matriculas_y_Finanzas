package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.HistorialEstadoMatriculaResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import pe.edu.upeu.lamb.matricula.entity.HistorialEstadoMatricula;
import pe.edu.upeu.lamb.matricula.mapper.HistorialEstadoMatriculaMapper;
import pe.edu.upeu.lamb.matricula.repository.HistorialEstadoMatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Solo lectura desde la API (no se expone POST/PUT/DELETE): es un registro de auditoria que
 * MatriculaService alimenta automaticamente en cada transicion de estado.
 */
@Service
@RequiredArgsConstructor
public class HistorialEstadoMatriculaService {

    private final HistorialEstadoMatriculaRepository historialEstadoMatriculaRepository;
    private final HistorialEstadoMatriculaMapper historialEstadoMatriculaMapper;

    public List<HistorialEstadoMatriculaResponse> listarPorMatricula(Long idMatricula) {
        return historialEstadoMatriculaRepository.findByIdMatriculaOrderByFechaAsc(idMatricula).stream()
                .map(historialEstadoMatriculaMapper::toResponse)
                .toList();
    }

    public void registrar(Long idMatricula, EstadoMatricula estadoAnterior, EstadoMatricula estadoNuevo) {
        historialEstadoMatriculaRepository.save(HistorialEstadoMatricula.builder()
                .idMatricula(idMatricula)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .fecha(Instant.now())
                .build());
    }
}

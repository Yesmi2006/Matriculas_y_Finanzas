package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.SeccionRequest;
import pe.edu.upeu.lamb.matricula.dto.SeccionResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import pe.edu.upeu.lamb.matricula.entity.EstadoSeccion;
import pe.edu.upeu.lamb.matricula.entity.Seccion;
import pe.edu.upeu.lamb.matricula.exception.ResourceNotFoundException;
import pe.edu.upeu.lamb.matricula.mapper.SeccionMapper;
import pe.edu.upeu.lamb.matricula.repository.DetalleMatriculaRepository;
import pe.edu.upeu.lamb.matricula.repository.SeccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeccionService {

    private static final EnumSet<EstadoMatricula> ESTADOS_QUE_OCUPAN_CUPO =
            EnumSet.of(EstadoMatricula.REGISTRADA, EstadoMatricula.CONFIRMADA);

    private final SeccionRepository seccionRepository;
    private final DetalleMatriculaRepository detalleMatriculaRepository;
    private final SeccionMapper seccionMapper;

    public List<SeccionResponse> listar(Long idPeriodoAcademico, EstadoSeccion estado) {
        List<Seccion> secciones = idPeriodoAcademico != null
                ? seccionRepository.findByIdPeriodoAcademico(idPeriodoAcademico)
                : seccionRepository.findAll();
        if (estado != null) {
            secciones = secciones.stream().filter(s -> s.getEstado() == estado).toList();
        }
        return secciones.stream().map(this::toResponse).toList();
    }

    public SeccionResponse obtener(Long id) {
        return toResponse(buscarOFallar(id));
    }

    public SeccionResponse crear(SeccionRequest request) {
        return toResponse(seccionRepository.save(seccionMapper.toEntity(request)));
    }

    public SeccionResponse actualizar(Long id, SeccionRequest request) {
        Seccion seccion = buscarOFallar(id);
        seccion.setIdCurso(request.getIdCurso());
        seccion.setIdPeriodoAcademico(request.getIdPeriodoAcademico());
        seccion.setCupoMaximo(request.getCupoMaximo());
        return toResponse(seccionRepository.save(seccion));
    }

    public void eliminar(Long id) {
        seccionRepository.delete(buscarOFallar(id));
    }

    public SeccionResponse cerrar(Long id) {
        Seccion seccion = buscarOFallar(id);
        seccion.setEstado(EstadoSeccion.CERRADA);
        return toResponse(seccionRepository.save(seccion));
    }

    public SeccionResponse abrir(Long id) {
        Seccion seccion = buscarOFallar(id);
        seccion.setEstado(EstadoSeccion.ABIERTA);
        return toResponse(seccionRepository.save(seccion));
    }

    Seccion buscarOFallar(Long id) {
        return seccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seccion no encontrada: " + id));
    }

    long cupoOcupado(Long idSeccion) {
        return detalleMatriculaRepository.contarMatriculadosEnSeccion(idSeccion, ESTADOS_QUE_OCUPAN_CUPO);
    }

    private SeccionResponse toResponse(Seccion seccion) {
        return seccionMapper.toResponse(seccion, cupoOcupado(seccion.getId()));
    }
}

package pe.edu.upeu.lamb.matricula.service;

import pe.edu.upeu.lamb.matricula.dto.ReporteResumenPeriodoResponse;
import pe.edu.upeu.lamb.matricula.dto.ReporteSeccionResponse;
import pe.edu.upeu.lamb.matricula.entity.EstadoMatricula;
import pe.edu.upeu.lamb.matricula.entity.Matricula;
import pe.edu.upeu.lamb.matricula.entity.Seccion;
import pe.edu.upeu.lamb.matricula.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteMatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final SeccionService seccionService;

    public ReporteSeccionResponse porSeccion(Long idSeccion) {
        Seccion seccion = seccionService.buscarOFallar(idSeccion);
        int matriculados = (int) seccionService.cupoOcupado(idSeccion);
        return ReporteSeccionResponse.builder()
                .idSeccion(idSeccion)
                .cupoMaximo(seccion.getCupoMaximo())
                .matriculados(matriculados)
                .cupoDisponible(Math.max(0, seccion.getCupoMaximo() - matriculados))
                .build();
    }

    public ReporteResumenPeriodoResponse resumenPorPeriodo(String periodo) {
        List<Matricula> matriculas = matriculaRepository.findAll().stream()
                .filter(m -> m.getPeriodo().equals(periodo))
                .toList();
        long registradas = matriculas.stream().filter(m -> m.getEstado() == EstadoMatricula.REGISTRADA).count();
        long confirmadas = matriculas.stream().filter(m -> m.getEstado() == EstadoMatricula.CONFIRMADA).count();
        long anuladas = matriculas.stream().filter(m -> m.getEstado() == EstadoMatricula.ANULADA).count();
        return ReporteResumenPeriodoResponse.builder()
                .periodo(periodo)
                .registradas(registradas)
                .confirmadas(confirmadas)
                .anuladas(anuladas)
                .total(matriculas.size())
                .build();
    }
}

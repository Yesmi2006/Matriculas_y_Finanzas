package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.HistorialEstadoMatricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoMatriculaRepository extends JpaRepository<HistorialEstadoMatricula, Long> {
    List<HistorialEstadoMatricula> findByIdMatriculaOrderByFechaAsc(Long idMatricula);
}

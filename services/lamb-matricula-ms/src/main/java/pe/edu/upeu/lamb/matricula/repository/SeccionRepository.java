package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeccionRepository extends JpaRepository<Seccion, Long> {
    List<Seccion> findByIdPeriodoAcademico(Long idPeriodoAcademico);
}

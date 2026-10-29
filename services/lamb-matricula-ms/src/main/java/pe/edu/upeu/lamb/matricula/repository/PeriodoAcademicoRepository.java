package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.EstadoPeriodo;
import pe.edu.upeu.lamb.matricula.entity.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Long> {
    Optional<PeriodoAcademico> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<PeriodoAcademico> findByEstado(EstadoPeriodo estado);
}

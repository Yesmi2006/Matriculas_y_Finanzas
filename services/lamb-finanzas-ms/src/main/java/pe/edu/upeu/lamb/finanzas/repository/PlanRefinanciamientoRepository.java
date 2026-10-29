package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.PlanRefinanciamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRefinanciamientoRepository extends JpaRepository<PlanRefinanciamiento, Long> {
    List<PlanRefinanciamiento> findByIdPersona(Long idPersona);
}

package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.LimiteCreditosPorCiclo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LimiteCreditosPorCicloRepository extends JpaRepository<LimiteCreditosPorCiclo, Long> {
    Optional<LimiteCreditosPorCiclo> findByCiclo(Integer ciclo);

    boolean existsByCiclo(Integer ciclo);
}

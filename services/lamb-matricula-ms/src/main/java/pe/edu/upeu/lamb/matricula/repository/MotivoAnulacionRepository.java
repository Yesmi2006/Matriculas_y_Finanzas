package pe.edu.upeu.lamb.matricula.repository;

import pe.edu.upeu.lamb.matricula.entity.MotivoAnulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotivoAnulacionRepository extends JpaRepository<MotivoAnulacion, Long> {
    Optional<MotivoAnulacion> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}

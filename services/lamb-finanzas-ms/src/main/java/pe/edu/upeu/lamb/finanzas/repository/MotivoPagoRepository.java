package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.MotivoPago;
import pe.edu.upeu.lamb.finanzas.entity.TipoMotivoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MotivoPagoRepository extends JpaRepository<MotivoPago, Long> {
    Optional<MotivoPago> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    List<MotivoPago> findByTipo(TipoMotivoPago tipo);
}

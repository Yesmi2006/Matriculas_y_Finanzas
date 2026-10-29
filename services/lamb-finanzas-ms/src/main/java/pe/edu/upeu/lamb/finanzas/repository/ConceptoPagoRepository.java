package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.ConceptoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConceptoPagoRepository extends JpaRepository<ConceptoPago, Long> {
    Optional<ConceptoPago> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}

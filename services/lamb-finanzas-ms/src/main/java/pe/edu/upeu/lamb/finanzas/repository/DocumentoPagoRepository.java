package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.DocumentoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentoPagoRepository extends JpaRepository<DocumentoPago, Long> {
    List<DocumentoPago> findByIdPago(Long idPago);

    Optional<DocumentoPago> findByNumero(String numero);

    boolean existsByNumero(String numero);
}

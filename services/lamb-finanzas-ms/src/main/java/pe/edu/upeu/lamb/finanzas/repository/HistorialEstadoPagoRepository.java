package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.HistorialEstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoPagoRepository extends JpaRepository<HistorialEstadoPago, Long> {
    List<HistorialEstadoPago> findByIdPagoOrderByFechaAsc(Long idPago);
}

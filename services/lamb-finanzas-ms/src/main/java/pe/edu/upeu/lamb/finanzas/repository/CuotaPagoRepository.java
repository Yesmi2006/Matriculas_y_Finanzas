package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.CuotaPago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuotaPagoRepository extends JpaRepository<CuotaPago, Long> {
    List<CuotaPago> findByIdPagoOrderByNumeroCuotaAsc(Long idPago);

    boolean existsByIdPagoAndNumeroCuota(Long idPago, Integer numeroCuota);
}

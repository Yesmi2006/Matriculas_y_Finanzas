package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.TransaccionPasarela;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionPasarelaRepository extends JpaRepository<TransaccionPasarela, Long> {
    List<TransaccionPasarela> findByIdPagoOrderByFechaDesc(Long idPago);
}

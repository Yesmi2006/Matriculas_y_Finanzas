package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.DescuentoAplicado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescuentoAplicadoRepository extends JpaRepository<DescuentoAplicado, Long> {
    List<DescuentoAplicado> findByIdPersona(Long idPersona);

    List<DescuentoAplicado> findByIdPago(Long idPago);
}

package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {
    List<Pago> findByIdPersona(Long idPersona);

    Optional<Pago> findByMatriculaId(Long matriculaId);
}

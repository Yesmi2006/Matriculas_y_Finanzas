package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.MetodoPagoConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetodoPagoConfigRepository extends JpaRepository<MetodoPagoConfig, Long> {
    Optional<MetodoPagoConfig> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}

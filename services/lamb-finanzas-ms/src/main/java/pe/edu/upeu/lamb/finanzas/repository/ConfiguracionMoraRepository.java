package pe.edu.upeu.lamb.finanzas.repository;

import pe.edu.upeu.lamb.finanzas.entity.ConfiguracionMora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracionMoraRepository extends JpaRepository<ConfiguracionMora, Long> {
    Optional<ConfiguracionMora> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
